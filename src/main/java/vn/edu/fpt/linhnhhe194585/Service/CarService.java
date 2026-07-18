package vn.edu.fpt.linhnhhe194585.Service;

import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> getAllCars();
    List<Car> searchCars(String name, String color, Integer capacity);
    Optional<Car> getCarById(Integer id);
    Car saveCar(Car car);
    boolean deleteCar(Integer id); // Delete if no rental records; else change status to Maintenance/Unavailable
    List<CarProducer> getAllProducers();
    Optional<CarProducer> getProducerById(Integer id);
    CarProducer saveProducer(CarProducer producer);
}
