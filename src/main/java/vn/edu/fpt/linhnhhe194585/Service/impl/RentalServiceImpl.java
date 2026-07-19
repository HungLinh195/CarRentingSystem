package vn.edu.fpt.linhnhhe194585.Service.impl;

import java.math.BigDecimal;
import vn.edu.fpt.linhnhhe194585.Entity.*;
import vn.edu.fpt.linhnhhe194585.Responsitory.*;
import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import vn.edu.fpt.linhnhhe194585.Entity.Review;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarRentalRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CustomerRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.ReviewRepository;
import vn.edu.fpt.linhnhhe194585.Service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalServiceImpl implements RentalService {
    @Autowired
    private CarRentalRepository carRentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public BigDecimal calculateTotalRevenue(List<CarRental> rentals) {
        if (rentals == null) {
            return BigDecimal.ZERO;
        }
        return rentals.stream()
                .map(CarRental::getRentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<CarRental> getAllRentals() {
        return carRentalRepository.findAll();
    }

    @Override
    public Optional<CarRental> getRentalById(Integer id) {
        return carRentalRepository.findById(id);
    }

    @Override
    public List<CarRental> getRentalsByCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
        return carRentalRepository.findByCustomer(customer);
    }

    @Override
    @Transactional
    public List<CarRental> createRentals(Integer customerId, List<Integer> carIds, LocalDate pickupDate, LocalDate returnDate) {
        if (pickupDate.isAfter(returnDate)) {
            throw new IllegalArgumentException("Pickup date must be before return date");
        }
        if (pickupDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Pickup date cannot be in the past");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        long days = ChronoUnit.DAYS.between(pickupDate, returnDate);
        if (days <= 0) days = 1; // Minimum 1 day rent

        List<CarRental> rentals = new ArrayList<>();
        for (Integer carId : carIds) {
            Car car = carRepository.findById(carId)
                    .orElseThrow(() -> new IllegalArgumentException("Car not found: " + carId));
            
            if (!"Available".equalsIgnoreCase(car.getStatus())) {
                throw new IllegalStateException("Car " + car.getCarName() + " is not available for rent");
            }

            // Calculate rent price
            BigDecimal totalPrice = car.getRentPrice().multiply(BigDecimal.valueOf(days));

            CarRental rental = CarRental.builder()
                    .customer(customer)
                    .car(car)
                    .pickupDate(pickupDate)
                    .returnDate(returnDate)
                    .rentPrice(totalPrice)
                    .status("Active")
                    .build();

            // Set car status to Rented
            car.setStatus("Rented");
            carRepository.save(car);

            rentals.add(carRentalRepository.save(rental));
        }

        return rentals;
    }

    @Override
    @Transactional
    public List<CarRental> createRentals(Integer customerId, String carIdsStr, String pickupDateStr, String returnDateStr) {
        if (carIdsStr == null || carIdsStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Danh sách xe thuê không được để trống");
        }
        List<Integer> carIds = Arrays.stream(carIdsStr.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        LocalDate pickupDate = LocalDate.parse(pickupDateStr);
        LocalDate returnDate = LocalDate.parse(returnDateStr);

        return createRentals(customerId, carIds, pickupDate, returnDate);
    }

    @Override
    @Transactional
    public CarRental updateRentalStatus(Integer rentalId, String status) {
        CarRental rental = carRentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental record not found"));

        rental.setStatus(status);

        if ("Completed".equalsIgnoreCase(status)) {
            Car car = rental.getCar();
            car.setStatus("Available");
            carRepository.save(car);
        }

        return carRentalRepository.save(rental);
    }

    @Override
    public List<CarRental> getReport(LocalDate startDate, LocalDate endDate) {
        return carRentalRepository.findRentalsBetweenDates(startDate, endDate);
    }

    @Override
    public Review addReview(Integer rentalId, Integer stars, String comment) {
        CarRental rental = carRentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental record not found"));
        
        if (!"Completed".equalsIgnoreCase(rental.getStatus())) {
            throw new IllegalStateException("Can only review completed rentals");
        }

        if (reviewRepository.findByCarRental(rental).isPresent()) {
            throw new IllegalStateException("Đơn thuê xe này đã được đánh giá rồi.");
        }

        Review review = Review.builder()
                .carRental(rental)
                .reviewStar(stars)
                .comment(comment)
                .build();

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForCar(Integer carId) {
        return reviewRepository.findByCarId(carId);
    }

    @Override
    public List<Integer> getReviewedRentalIds(Integer customerId) {
        return reviewRepository.findReviewedRentalIdsByCustomerId(customerId);
    }
}
