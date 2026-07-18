package vn.edu.fpt.linhnhhe194585.Service.impl;

import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarProducerRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarRentalRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarRepository;
import vn.edu.fpt.linhnhhe194585.Service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CarProducerRepository carProducerRepository;

    @Autowired
    private CarRentalRepository carRentalRepository;

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public List<Car> searchCars(String name, String color, Integer capacity) {
        return carRepository.searchCars(name, color, capacity);
    }

    @Override
    public Optional<Car> getCarById(Integer id) {
        return carRepository.findById(id);
    }

    @Override
    public Car saveCar(Car car) {
        if (car.getStatus() == null) {
            car.setStatus("Available");
        }
        return carRepository.save(car);
    }

    @Override
    public boolean deleteCar(Integer id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            boolean hasRentals = !carRentalRepository.findByCar(car).isEmpty();
            if (hasRentals) {
                // If it has rental records, change status to Maintenance
                car.setStatus("Maintenance");
                carRepository.save(car);
                return false; // Did not physically delete
            } else {
                carRepository.delete(car);
                return true; // Deleted successfully
            }
        }
        return false;
    }

    @Override
    public List<CarProducer> getAllProducers() {
        return carProducerRepository.findAll();
    }

    @Override
    public Optional<CarProducer> getProducerById(Integer id) {
        return carProducerRepository.findById(id);
    }

    @Override
    public CarProducer saveProducer(CarProducer producer) {
        return carProducerRepository.save(producer);
    }
}
