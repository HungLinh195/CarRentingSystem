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
        if (fullName == null || !fullName.matches("^[\\p{L}\\s]+$")) {
            throw new IllegalArgumentException("Họ và tên chỉ được phép nhập chữ!");
        }
        if (mobile == null || !mobile.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Số điện thoại phải bao gồm đúng 10 chữ số!");
        }
        if (identityCard == null || !identityCard.matches("^\\d+$")) {
            throw new IllegalArgumentException("Số CMND/CCCD chỉ được phép nhập số!");
        }

        if (accountRepository.findByEmail(account.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        account.setRole("Customer");
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
    @Transactional
    public Account register(String email, String password, String name, String fullName, String mobile, String birthday, String identityCard, String licenceNumber, String licenceDate) {
        Account account = Account.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
        return register(account, fullName, mobile, birthday, identityCard, licenceNumber, licenceDate);
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

    @Override
    @Transactional
    public void changePassword(Integer accountId, String currentPassword, String newPassword, String confirmNewPassword) {
        if (newPassword == null || !newPassword.equals(confirmNewPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu mới không khớp!");
        }
        changePassword(accountId, currentPassword, newPassword);
    }

    @Override
    @Transactional
    public Account updateProfile(Integer accountId, String name) {
        if (name == null || !name.matches("^[\\p{L}\\s]+$")) {
            throw new IllegalArgumentException("Họ và tên chỉ được phép nhập chữ!");
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản."));
        account.setName(name);
        return accountRepository.save(account);
    }
}
