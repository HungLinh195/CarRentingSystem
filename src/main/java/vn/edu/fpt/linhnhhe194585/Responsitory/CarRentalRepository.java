package vn.edu.fpt.linhnhhe194585.Responsitory;

import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarRental;
import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRentalRepository extends JpaRepository<CarRental, Integer> {
    List<CarRental> findByCustomer(Customer customer);
    List<CarRental> findByCar(Car car);
    
    @Query("SELECT cr FROM CarRental cr WHERE cr.pickupDate >= :startDate AND cr.returnDate <= :endDate ORDER BY cr.rentPrice DESC")
    List<CarRental> findRentalsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
