package vn.edu.fpt.linhnhhe194585.Service.impl;

import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import vn.edu.fpt.linhnhhe194585.Responsitory.AccountRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CustomerRepository;
import vn.edu.fpt.linhnhhe194585.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Optional<Account> login(String email, String password) {
        return accountRepository.findByEmail(email)
                .filter(a -> a.getPassword().equals(password));
    }

    @Override
    @Transactional
    public Account register(Account account, String fullName, String mobile, String birthday, String identityCard, String licenceNumber, String licenceDate) {
        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        account.setRole("Customer"); // Customer role by default
        Account savedAccount = accountRepository.save(account);

        Customer customer = Customer.builder()
                .fullName(fullName)
                .mobile(mobile)
                .birthday(LocalDate.parse(birthday))
                .identityCard(identityCard)
                .licenceNumber(licenceNumber)
                .licenceDate(LocalDate.parse(licenceDate))
                .account(savedAccount)
                .build();
        customerRepository.save(customer);

        return savedAccount;
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Optional<Account> findById(Integer id) {
        return accountRepository.findById(id);
    }

    @Override
    @Transactional
    public void changePassword(Integer accountId, String currentPassword, String newPassword) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        if (!account.getPassword().equals(currentPassword)) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không chính xác.");
        }
        account.setPassword(newPassword);
        accountRepository.save(account);
    }
}
