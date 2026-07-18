package vn.edu.fpt.linhnhhe194585.Responsitory;

import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarProducerRepository extends JpaRepository<CarProducer, Integer> {
}
