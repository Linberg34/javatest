import com.carrental.entities.Car;
import com.carrental.repository.CarRepository;
import com.carrental.service.implementations.CarServiceImpl;
import com.example.common.enums.CarStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car testCar;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testCar = new Car();
        testCar.setId(testId);
        testCar.setMake("Toyota");
        testCar.setModel("Camry");
        testCar.setPlateNumber("ABC123");
        testCar.setStatus(CarStatus.FREE);
    }

    @Test
    void listAll_shouldReturnEmptyList_whenNoCars() {
        when(carRepository.findAll()).thenReturn(Collections.emptyList());

        List<Car> result = carService.listAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void listAll_shouldReturnCars_whenCarsExist() {
        List<Car> cars = List.of(testCar);
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void listAvailable_shouldReturnEmptyList_whenNoAvailableCars() {
        when(carRepository.findByStatus(CarStatus.FREE)).thenReturn(Collections.emptyList());

        List<Car> result = carService.listAvailable();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findByStatus(CarStatus.FREE);
    }

    @Test
    void listAvailable_shouldReturnCars_whenAvailableCarsExist() {
        List<Car> cars = List.of(testCar);
        when(carRepository.findByStatus(CarStatus.FREE)).thenReturn(cars);

        List<Car> result = carService.listAvailable();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(CarStatus.FREE, result.get(0).getStatus());
        verify(carRepository, times(1)).findByStatus(CarStatus.FREE);
    }

    @Test
    void getById_shouldReturnCar_whenCarExists() {
        when(carRepository.findById(testId)).thenReturn(Optional.of(testCar));

        Car result = carService.getById(testId);

        assertNotNull(result);
        assertEquals("Camry", result.getModel());
        verify(carRepository, times(1)).findById(testId);
    }

    @Test
    void getById_shouldThrowException_whenCarNotFound() {
        when(carRepository.findById(testId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                carService.getById(testId));
        assertTrue(exception.getMessage().contains("Car not found: " + testId));
        verify(carRepository, times(1)).findById(testId);
    }

    @Test
    void create_shouldReturnSavedCar_withFreeStatus() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        Car result = carService.create(new Car());

        assertNotNull(result);
        assertEquals(CarStatus.FREE, result.getStatus());
        verify(carRepository, times(1)).save(any(Car.class));
    }


    @Test
    void update_shouldThrowException_whenCarNotFound() {
        when(carRepository.findById(testId)).thenReturn(Optional.empty());

        Car updatedCar = new Car();
        updatedCar.setMake("Honda");

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                carService.update(testId, updatedCar));
        assertTrue(exception.getMessage().contains("Car not found: " + testId));
        verify(carRepository, times(1)).findById(testId);
        verify(carRepository, never()).save(any());
    }

    @Test
    void changeStatus_shouldReturnCarWithNewStatus_whenCarExists() {
        when(carRepository.findById(testId)).thenReturn(Optional.of(testCar));
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        Car result = carService.changeStatus(testId, CarStatus.RENTED);

        assertNotNull(result);
        assertEquals(CarStatus.RENTED, result.getStatus());
        verify(carRepository, times(1)).findById(testId);
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void changeStatus_shouldThrowException_whenCarNotFound() {
        when(carRepository.findById(testId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                carService.changeStatus(testId, CarStatus.RENTED));
        assertTrue(exception.getMessage().contains("Car not found: " + testId));
        verify(carRepository, times(1)).findById(testId);
        verify(carRepository, never()).save(any());
    }

}