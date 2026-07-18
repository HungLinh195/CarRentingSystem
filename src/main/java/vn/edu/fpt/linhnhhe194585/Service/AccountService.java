package vn.edu.fpt.linhnhhe194585.Service;

import vn.edu.fpt.linhnhhe194585.Entity.Account;
import java.util.Optional;

public interface AccountService {
    Optional<Account> login(String email, String password);
    Account register(Account account, String fullName, String mobile, String birthday, String identityCard, String licenceNumber, String licenceDate);
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(Integer id);
    void changePassword(Integer accountId, String currentPassword, String newPassword);
}
