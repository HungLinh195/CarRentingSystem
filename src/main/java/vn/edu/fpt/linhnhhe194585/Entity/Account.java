package vn.edu.fpt.linhnhhe194585.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder

@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AccountID", nullable = false)
    private Integer id;

    @Column(name = "AccountName", nullable = false, columnDefinition = "NVARCHAR(100)")
    private String name;

    @Column(name = "Email", nullable = false, unique = true, length = 200, columnDefinition = "VARCHAR(200)")
    private String email;

    @Column(name = "Password", nullable = false, length = 200, columnDefinition = "VARCHAR(200)")
    private String password;

    @Column(name = "Role", nullable = false, columnDefinition = "NVARCHAR(10)")
    private String role;

    @OneToOne(mappedBy = "account")
    private Customer customer;
}
