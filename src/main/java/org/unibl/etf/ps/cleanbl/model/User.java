package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@AllArgsConstructor
@Entity(name = "User")
@Table(
        name = "User",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email"),
                @UniqueConstraint(name = "user_username_unique", columnNames = "username")
        }
)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(
            updatable = false
    )
    private Long id;

    @Column(
            nullable = false
    )
    @NotBlank(message = "Username is required")
    private String username;

    @Column(
            nullable = false
    )
    @NotBlank(message = "First name is required")
    private String firstName;

    @Column(
            nullable = false
    )
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Column(
            nullable = false
    )
    @Email
    @NotBlank(message = "E-mail is required")
    private String email;

    @Column(
            nullable = false
    )
    @NotBlank(message = "Password is required")
    private String password;

    private Instant created;

    @PrePersist
    public void createdAt() {
        created = Instant.now();
    }
}
