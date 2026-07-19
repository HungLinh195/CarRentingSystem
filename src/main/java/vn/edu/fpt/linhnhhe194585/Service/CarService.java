package vn.edu.fpt.linhnhhe194585.Service;

import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> getAllCars();
    List<Car> searchCars(String name, String color, Integer capacity);
    List<Car> getCars(String name, String color, Integer capacity);
    Optional<Car> getCarById(Integer id);
    Car saveCar(Car car);
    Car saveCar(Integer id, String carName, Integer carModelYear, String color, Integer capacity, String description, String importDateStr, Integer producerId, BigDecimal rentPrice, String status);
    boolean deleteCar(Integer id); // Delete if no rental records; else change status to Maintenance/Unavailable
    List<CarProducer> getAllProducers();
    Optional<CarProducer> getProducerById(Integer id);
    CarProducer saveProducer(CarProducer producer);
}
