package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Content is required")
    private String content;

    @ManyToOne
    @JoinColumn(
            name = "senderId"
    )
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "receiverId"
    )
    private User receiver;
}
