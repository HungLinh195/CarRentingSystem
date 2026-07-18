package vn.edu.fpt.linhnhhe194585.Config;

import java.math.BigDecimal;
import vn.edu.fpt.linhnhhe194585.Entity.*;
import vn.edu.fpt.linhnhhe194585.Responsitory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import vn.edu.fpt.linhnhhe194585.Entity.Account;
import vn.edu.fpt.linhnhhe194585.Entity.Car;
import vn.edu.fpt.linhnhhe194585.Entity.CarProducer;
import vn.edu.fpt.linhnhhe194585.Entity.Customer;
import vn.edu.fpt.linhnhhe194585.Responsitory.AccountRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarProducerRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CarRepository;
import vn.edu.fpt.linhnhhe194585.Responsitory.CustomerRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CarProducerRepository carProducerRepository;

    @Autowired
    private CarRepository carRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if no accounts exist
        if (accountRepository.count() == 0) {
            System.out.println("====== SEEDING DATABASE WITH DEFAULT DATA ======");

            // 1. Seed Accounts
            Account admin = Account.builder()
                    .name("admin")
                    .email("admin@carrent.com")
                    .password("admin123")
                    .role("Admin")
                    .build();
            accountRepository.save(admin);

            Account customerAcc = Account.builder()
                    .name("customer")
                    .email("customer@carrent.com")
                    .password("customer123")
                    .role("Customer")
                    .build();
            Account savedCustomerAcc = accountRepository.save(customerAcc);

            // 2. Seed Customer details
            Customer customerObj = Customer.builder()
                    .fullName("Nguyễn Văn Khách")
                    .mobile("0987654321")
                    .birthday(LocalDate.of(1995, 10, 15))
                    .identityCard("012345678912")
                    .licenceNumber("B2-9876543")
                    .licenceDate(LocalDate.of(2021, 6, 20))
                    .account(savedCustomerAcc)
                    .build();
            customerRepository.save(customerObj);

            // 3. Seed Producers
            CarProducer toyota = CarProducer.builder()
                    .producerName("Toyota")
                    .address("Toyota City, Aichi")
                    .country("Nhật Bản")
                    .build();
            toyota = carProducerRepository.save(toyota);

            CarProducer honda = CarProducer.builder()
                    .producerName("Honda")
                    .address("Minato, Tokyo")
                    .country("Nhật Bản")
                    .build();
            honda = carProducerRepository.save(honda);

            CarProducer ford = CarProducer.builder()
                    .producerName("Ford")
                    .address("Dearborn, Michigan")
                    .country("Hoa Kỳ")
                    .build();
            ford = carProducerRepository.save(ford);

            // 4. Seed Cars
            Car car1 = Car.builder()
                    .carName("Toyota Vios 1.5G")
                    .carModelYear(2022)
                    .color("Trắng")
                    .capacity(5)
                    .description("Dòng xe sedan 5 chỗ quốc dân, tiết kiệm nhiên liệu, vận hành bền bỉ và điều hòa làm lạnh cực nhanh.")
                    .importDate(LocalDate.of(2025, 3, 10))
                    .carProducer(toyota)
                    .rentPrice(BigDecimal.valueOf(600000.0))
                    .status("Available")
                    .build();
            carRepository.save(car1);

            Car car2 = Car.builder()
                    .carName("Honda Civic RS")
                    .carModelYear(2023)
                    .color("Đỏ")
                    .capacity(5)
                    .description("Mẫu sedan thể thao phong cách mạnh mẽ, trang bị động cơ tăng áp 1.5L Turbo, vận hành bốc và đầm chắc.")
                    .importDate(LocalDate.of(2025, 4, 15))
                    .carProducer(honda)
                    .rentPrice(BigDecimal.valueOf(900000.0))
                    .status("Available")
                    .build();
            carRepository.save(car2);

            Car car3 = Car.builder()
                    .carName("Ford Ranger Wildtrak")
                    .carModelYear(2022)
                    .color("Xám")
                    .capacity(5)
                    .description("Bán tải bán chạy số một Việt Nam, động cơ Bi-Turbo mạnh mẽ, gầm cao vượt địa hình tốt, phù hợp đi phượt dã ngoại.")
                    .importDate(LocalDate.of(2025, 5, 20))
                    .carProducer(ford)
                    .rentPrice(BigDecimal.valueOf(1200000.0))
                    .status("Available")
                    .build();
            carRepository.save(car3);

            Car car4 = Car.builder()
                    .carName("Toyota Fortuner Legender")
                    .carModelYear(2023)
                    .color("Đen")
                    .capacity(7)
                    .description("SUV 7 chỗ rộng rãi, hầm hố, điều hòa 2 giàn lạnh độc lập, máy dầu tiết kiệm nhiên liệu, phù hợp cho gia đình đông người du lịch.")
                    .importDate(LocalDate.of(2025, 6, 25))
                    .carProducer(toyota)
                    .rentPrice(BigDecimal.valueOf(1500000.0))
                    .status("Available")
                    .build();
            carRepository.save(car4);

            System.out.println("====== DATABASE SEEDING COMPLETED ======");
        }
    }
}
