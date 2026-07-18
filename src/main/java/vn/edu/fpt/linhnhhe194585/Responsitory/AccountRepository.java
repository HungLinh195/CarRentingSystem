package vn.edu.fpt.linhnhhe194585.Responsitory;

import vn.edu.fpt.linhnhhe194585.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByName(String name);
    Optional<Account> findByEmail(String email);
}
