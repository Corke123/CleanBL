package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ContactUsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @Email(message = "Invalid email")
    @NotBlank(message = "E-mail is required")
    private String email;

    private boolean replied;

    private String response;

    private LocalDate createdAt;

    @PrePersist
    private void addDate() {
        this.createdAt = LocalDate.now();
    }
}
