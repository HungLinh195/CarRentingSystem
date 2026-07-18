package vn.edu.fpt.linhnhhe194585.Service;

import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Entity.Review;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RentalService {
    List<CarRental> getAllRentals();
    Optional<CarRental> getRentalById(Integer id);
    List<CarRental> getRentalsByCustomer(Integer customerId);
    List<CarRental> createRentals(Integer customerId, List<Integer> carIds, LocalDate pickupDate, LocalDate returnDate);
    CarRental updateRentalStatus(Integer rentalId, String status);
    List<CarRental> getReport(LocalDate startDate, LocalDate endDate);
    Review addReview(Integer rentalId, Integer stars, String comment);
    List<Review> getReviewsForCar(Integer carId);
}
