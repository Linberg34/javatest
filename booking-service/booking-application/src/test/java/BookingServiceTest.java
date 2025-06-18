
import com.carrental.booking.application.service.implementations.BookingServiceImpl;
import com.carrental.booking.domain.entity.Booking;
import com.carrental.booking.domain.event.BookingRequestedEvent;
import com.carrental.booking.domain.repository.BookingRepository;
import com.example.common.enums.BookingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private Clock clock;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private UUID testCarId;
    private UUID testUserId;
    private UUID testBookingId;
    private Instant testFrom;
    private Instant testTo;
    private Instant testNow;

    @BeforeEach
    void setUp() {
        testCarId = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testBookingId = UUID.randomUUID();
        testFrom = Instant.parse("2025-06-18T10:00:00Z");
        testTo = Instant.parse("2025-06-18T12:00:00Z");
        testNow = Instant.parse("2025-06-17T15:53:00Z");
    }

    @Test
    void canBook_shouldReturnTrue_whenNoOverlappingBookings() {
        when(bookingRepository.findOverlapping(testCarId, testFrom, testTo)).thenReturn(Collections.emptyList());

        boolean result = bookingService.canBook(testCarId, testFrom, testTo);

        assertTrue(result);
        verify(bookingRepository, times(1)).findOverlapping(testCarId, testFrom, testTo);
    }

    @Test
    void canBook_shouldReturnFalse_whenOverlappingBookingsExist() {
        Booking overlappingBooking = new Booking();
        overlappingBooking.setCarId(testCarId);
        overlappingBooking.setRentFrom(testFrom.minusSeconds(3600));
        overlappingBooking.setRentTo(testTo.plusSeconds(3600));
        when(bookingRepository.findOverlapping(testCarId, testFrom, testTo))
                .thenReturn(List.of(overlappingBooking));

        boolean result = bookingService.canBook(testCarId, testFrom, testTo);

        assertFalse(result);
        verify(bookingRepository, times(1)).findOverlapping(testCarId, testFrom, testTo);
    }

    @Test
    void createBooking_shouldThrowException_whenCarIsNotAvailable() {
        Booking overlappingBooking = new Booking();
        overlappingBooking.setCarId(testCarId);
        overlappingBooking.setRentFrom(testFrom);
        overlappingBooking.setRentTo(testTo);
        when(bookingRepository.findOverlapping(testCarId, testFrom, testTo))
                .thenReturn(List.of(overlappingBooking));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                bookingService.createBooking(testCarId, testUserId, testFrom, testTo));
        assertEquals("Car is already booked for the given period", exception.getMessage());
        verify(bookingRepository, times(1)).findOverlapping(testCarId, testFrom, testTo);
        verify(bookingRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }


    @Test
    void finishRental_shouldReturnUpdatedBooking_whenBookingExists() {
        when(clock.instant()).thenReturn(testNow);
        Booking existingBooking = new Booking();
        existingBooking.setId(testBookingId);
        existingBooking.setStatus(BookingStatus.PENDING);
        when(bookingRepository.findById(testBookingId)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(existingBooking);

        Booking result = bookingService.finishRental(testBookingId);

        assertNotNull(result);
        assertEquals(BookingStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(bookingRepository, times(1)).findById(testBookingId);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void finishRental_shouldThrowException_whenBookingNotFound() {
        when(bookingRepository.findById(testBookingId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.finishRental(testBookingId));
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(testBookingId);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnBooking_whenBookingExists() {
        Booking existingBooking = new Booking();
        existingBooking.setId(testBookingId);
        when(bookingRepository.findById(testBookingId)).thenReturn(Optional.of(existingBooking));

        Booking result = bookingService.findById(testBookingId);

        assertNotNull(result);
        assertEquals(testBookingId, result.getId());
        verify(bookingRepository, times(1)).findById(testBookingId);
    }

    @Test
    void findById_shouldThrowException_whenBookingNotFound() {
        when(bookingRepository.findById(testBookingId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                bookingService.findById(testBookingId));
        assertEquals("Booking not found", exception.getMessage());
        verify(bookingRepository, times(1)).findById(testBookingId);
    }

    @Test
    void historyByUser_shouldReturnEmptyList_whenNoBookings() {
        when(bookingRepository.findByUserId(testUserId)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.historyByUser(testUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void historyByUser_shouldReturnBookings_whenBookingsExist() {
        List<Booking> bookings = List.of(new Booking());
        bookings.get(0).setUserId(testUserId);
        when(bookingRepository.findByUserId(testUserId)).thenReturn(bookings);

        List<Booking> result = bookingService.historyByUser(testUserId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void historyByCar_shouldReturnEmptyList_whenNoBookings() {
        when(bookingRepository.findByCarId(testCarId)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.historyByCar(testCarId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookingRepository, times(1)).findByCarId(testCarId);
    }

    @Test
    void historyByCar_shouldReturnBookings_whenBookingsExist() {
        List<Booking> bookings = List.of(new Booking());
        bookings.get(0).setCarId(testCarId);
        when(bookingRepository.findByCarId(testCarId)).thenReturn(bookings);

        List<Booking> result = bookingService.historyByCar(testCarId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingRepository, times(1)).findByCarId(testCarId);
    }
}