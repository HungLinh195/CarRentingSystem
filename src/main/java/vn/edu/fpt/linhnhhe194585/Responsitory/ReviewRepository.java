package vn.edu.fpt.linhnhhe194585.Responsitory;

import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    Optional<Review> findByCarRental(CarRental carRental);
    
    @Query("SELECT r FROM Review r WHERE r.carRental.car.id = :carId")
    List<Review> findByCarId(@Param("carId") Integer carId);
}
