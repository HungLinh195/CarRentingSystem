package vn.edu.fpt.linhnhhe194585.Service;

import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(Integer id);
    Customer saveCustomer(Customer customer);
    void deleteCustomer(Integer id);
    Customer updateProfile(Integer customerId, String fullName, String mobile, String birthdayStr, String identityCard, String licenceNumber, String licenceDateStr);
}
