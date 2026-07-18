package vn.edu.fpt.linhnhhe194585.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CarID")
    private Integer id;

    @Column(name = "CarName", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String carName;

    @Column(name = "CarModelYear", nullable = false)
    private Integer carModelYear;

    @Column(name = "Color", nullable = false, columnDefinition = "NVARCHAR(50)")
    private String color;

    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

    @Column(name = "Description", nullable = false, columnDefinition = "NVARCHAR(1000)")
    private String description;

    @Column(name = "ImportDate", nullable = false)
    private LocalDate importDate;

    @ManyToOne
    @JoinColumn(name = "ProducerID", nullable = false)
    private CarProducer carProducer;

    @Column(name = "RentPrice", nullable = false, precision = 10)
    private BigDecimal rentPrice;

    @Column(name = "Status", nullable = false, columnDefinition = "NVARCHAR(10)")
    private String status; // Available, Rented, Maintenance
}
