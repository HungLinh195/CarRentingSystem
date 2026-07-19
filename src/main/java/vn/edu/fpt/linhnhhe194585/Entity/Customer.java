package vn.edu.fpt.linhnhhe194585.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CustomerID", nullable = false)
    private Integer id;

    @Column(name = "FullName", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String fullName;

    @Column(name = "Mobile", nullable = false,  columnDefinition = "VARCHAR(15)")
    private String mobile;

    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "IdentityCard", nullable = false, columnDefinition = "VARCHAR(20)")
    private String identityCard;

    @Column(name = "LicenceNumber", nullable = false, columnDefinition = "VARCHAR(20)")
    private String licenceNumber;

    @Column(name = "LicenceDate", nullable = false)
    private LocalDate licenceDate;

    @OneToOne
    @JoinColumn(name = "AccountID")
    private Account account;

    @OneToMany(mappedBy = "customer")
    private java.util.Set<CarRental> rentals;
}
