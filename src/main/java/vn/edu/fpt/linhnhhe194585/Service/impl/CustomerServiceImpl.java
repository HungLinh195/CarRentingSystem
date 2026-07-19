package vn.edu.fpt.linhnhhe194585.Service.impl;

import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import vn.edu.fpt.linhnhhe194585.Responsitory.CustomerRepository;
import vn.edu.fpt.linhnhhe194585.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Customer updateProfile(Integer customerId, String fullName, String mobile, String birthdayStr, String identityCard, String licenceNumber, String licenceDateStr) {
        if (fullName == null || !fullName.matches("^[\\p{L}\\s]+$")) {
            throw new IllegalArgumentException("Họ và tên chỉ được phép nhập chữ!");
        }
        if (mobile == null || !mobile.matches("^\\d{10}$")) {
            throw new IllegalArgumentException("Số điện thoại phải bao gồm đúng 10 chữ số!");
        }
        if (identityCard == null || !identityCard.matches("^\\d+$")) {
            throw new IllegalArgumentException("Số CMND/CCCD chỉ được phép nhập số!");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin khách hàng."));

        customer.setFullName(fullName);
        customer.setMobile(mobile);
        customer.setBirthday(LocalDate.parse(birthdayStr));
        customer.setIdentityCard(identityCard);
        customer.setLicenceNumber(licenceNumber);
        customer.setLicenceDate(LocalDate.parse(licenceDateStr));

        return customerRepository.save(customer);
    }
}
