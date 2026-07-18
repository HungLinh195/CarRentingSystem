package vn.edu.fpt.linhnhhe194585.Responsitory;

import vn.edu.fpt.linhnhhe194585.Entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    List<Car> findByStatus(String status);
    
    @Query("SELECT c FROM Car c WHERE " +
           "(:name IS NULL OR :name = '' OR LOWER(c.carName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:color IS NULL OR :color = '' OR LOWER(c.color) = LOWER(:color)) AND " +
           "(:capacity IS NULL OR c.capacity = :capacity)")
    List<Car> searchCars(@Param("name") String name, 
                         @Param("color") String color, 
                         @Param("capacity") Integer capacity);
}
