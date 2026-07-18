package vn.edu.fpt.linhnhhe194585.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "CarProducer")

public class CarProducer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProducerID")
    private Integer id;

    @Column(name = "ProducerName", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String producerName;

    @Column(name = "Address", nullable = false, columnDefinition = "NVARCHAR(200)")
    private String address;

    @Column(name = "Country", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String country;
}
