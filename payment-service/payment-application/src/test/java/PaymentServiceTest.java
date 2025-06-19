import com.carrental.payment.application.service.implementations.PaymentServiceImpl;
import com.carrental.payment.domain.entity.Payment;
import com.carrental.payment.domain.event.PaymentProcessedEvent;
import com.carrental.payment.domain.repository.PaymentRepository;
import com.example.common.enums.PaymentStatus;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Clock fixedClock;
    private Instant fixedInstant;
    private UUID paymentId;
    private UUID bookingId;

    @BeforeEach
    void setUp() {
        fixedInstant = Instant.parse("2025-06-18T12:00:00Z");
        fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"));
        paymentId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
    }

    @Test
    void createPayment_ValidInput_ReturnsSavedPayment() {
        long amount = 1000L;
        Payment expectedPayment = new Payment();
        expectedPayment.setId(paymentId);
        expectedPayment.setBookingId(bookingId);
        expectedPayment.setAmount(amount);
        expectedPayment.setStatus(PaymentStatus.NEW_PAYMENT);
        expectedPayment.setCreatedAt(fixedInstant);
        expectedPayment.setUpdatedAt(fixedInstant);
        expectedPayment.setCreatedBy("system");

        when(paymentRepository.save(any(Payment.class))).thenReturn(expectedPayment);

        Payment result = paymentService.createPayment(bookingId, amount);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(bookingId, result.getBookingId());
        assertEquals(amount, result.getAmount());
        assertEquals(PaymentStatus.NEW_PAYMENT, result.getStatus());
        assertEquals(fixedInstant, result.getCreatedAt());
        assertEquals(fixedInstant, result.getUpdatedAt());
        assertEquals("system", result.getCreatedBy());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void createPayment_NonPositiveAmount_ThrowsIllegalArgumentException() {
        long invalidAmount = 0L;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.createPayment(bookingId, invalidAmount)
        );
        assertEquals("Amount must be positive", exception.getMessage());
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void pay_ValidNewPayment_ReturnsPaidPaymentAndSendsEvent() throws InterruptedException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setBookingId(bookingId);
        payment.setAmount(1000L);
        payment.setStatus(PaymentStatus.NEW_PAYMENT);
        payment.setCreatedAt(fixedInstant);
        payment.setUpdatedAt(fixedInstant);
        payment.setCreatedBy("system");

        Payment updatedPayment = new Payment();
        updatedPayment.setId(paymentId);
        updatedPayment.setBookingId(bookingId);
        updatedPayment.setAmount(1000L);
        updatedPayment.setStatus(PaymentStatus.PAID);
        updatedPayment.setCreatedAt(fixedInstant);
        updatedPayment.setUpdatedAt(fixedInstant);
        updatedPayment.setCreatedBy("system");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        Payment result = paymentService.pay(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals(fixedInstant, result.getUpdatedAt());

        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).save(any(Payment.class));
        verify(kafkaTemplate).send(eq("payment.processed"), any(PaymentProcessedEvent.class));
    }

    @Test
    void pay_PaymentNotFound_ThrowsIllegalArgumentException() {

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.pay(paymentId)
        );
        assertEquals("Payment not found: " + paymentId, exception.getMessage());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void pay_NonNewPayment_ThrowsIllegalStateException() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentService.pay(paymentId)
        );
        assertEquals("Payment can only be processed if it is new", exception.getMessage());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void cancel_ValidNonPaidPayment_ReturnsCancelledPayment() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setBookingId(bookingId);
        payment.setAmount(1000L);
        payment.setStatus(PaymentStatus.NEW_PAYMENT);
        payment.setCreatedAt(fixedInstant);
        payment.setUpdatedAt(fixedInstant);
        payment.setCreatedBy("system");

        Payment updatedPayment = new Payment();
        updatedPayment.setId(paymentId);
        updatedPayment.setBookingId(bookingId);
        updatedPayment.setAmount(1000L);
        updatedPayment.setStatus(PaymentStatus.CANCELLED);
        updatedPayment.setCreatedAt(fixedInstant);
        updatedPayment.setUpdatedAt(fixedInstant);
        updatedPayment.setCreatedBy("system");

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        Payment result = paymentService.cancel(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        assertEquals(PaymentStatus.CANCELLED, result.getStatus());
        assertEquals(fixedInstant, result.getUpdatedAt());

        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository).save(any(Payment.class));
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void cancel_PaidPayment_ThrowsIllegalStateException() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentService.cancel(paymentId)
        );
        assertEquals("Cannot cancel a paid payment", exception.getMessage());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void cancel_PaymentNotFound_ThrowsIllegalArgumentException() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.cancel(paymentId)
        );
        assertEquals("Payment not found: " + paymentId, exception.getMessage());
        verify(paymentRepository).findById(paymentId);
        verify(paymentRepository, never()).save(any());
        verify(kafkaTemplate, never()).send(anyString(), any());
    }

    @Test
    void getById_ExistingPayment_ReturnsPayment() {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setBookingId(bookingId);
        payment.setAmount(1000L);
        payment.setStatus(PaymentStatus.NEW_PAYMENT);

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getById(paymentId);

        assertNotNull(result);
        assertEquals(paymentId, result.getId());
        verify(paymentRepository).findById(paymentId);
    }

    @Test
    void getById_NonExistingPayment_ThrowsIllegalArgumentException() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.getById(paymentId)
        );
        assertEquals("Payment not found: " + paymentId, exception.getMessage());
        verify(paymentRepository).findById(paymentId);
    }

    @Test
    void getAll_ValidPageAndSize_ReturnsPaymentList() {
        int page = 0;
        int size = 10;
        Payment payment = new Payment();
        payment.setId(paymentId);
        List<Payment> payments = List.of(payment);

        when(paymentRepository.findAll(page, size)).thenReturn(payments);

        List<Payment> result = paymentService.getAll(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paymentId, result.get(0).getId());
        verify(paymentRepository).findAll(page, size);
    }

    @Test
    void getAll_InvalidPageOrSize_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.getAll(-1, 10)
        );
        assertEquals("Page must be non-negative and size must be positive", exception.getMessage());

        exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.getAll(0, 0)
        );
        assertEquals("Page must be non-negative and size must be positive", exception.getMessage());

        verify(paymentRepository, never()).findAll(anyInt(), anyInt());
    }

    @Test
    void getPendingPayments_ValidPageAndSize_ReturnsPendingPayments() {
        int page = 0;
        int size = 10;
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.NEW_PAYMENT);
        List<Payment> payments = List.of(payment);

        when(paymentRepository.findByStatus(PaymentStatus.NEW_PAYMENT, page, size)).thenReturn(payments);

        List<Payment> result = paymentService.getPendingPayments(page, size);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(paymentId, result.get(0).getId());
        verify(paymentRepository).findByStatus(PaymentStatus.NEW_PAYMENT, page, size);
    }

    @Test
    void getPendingPayments_InvalidPageOrSize_ThrowsIllegalArgumentException() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.getPendingPayments(-1, 10)
        );
        assertEquals("Page must be non-negative and size must be positive", exception.getMessage());

        exception = assertThrows(
                IllegalArgumentException.class,
                () -> paymentService.getPendingPayments(0, 0)
        );
        assertEquals("Page must be non-negative and size must be positive", exception.getMessage());

        verify(paymentRepository, never()).findByStatus(any(), anyInt(), anyInt());
    }
}