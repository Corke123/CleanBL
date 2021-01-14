package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email
    @NotBlank(message = "E-mail is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private Instant created;

    @PrePersist
    public void createdAt() {
        created = Instant.now();
    }
}
