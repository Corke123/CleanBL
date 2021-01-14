package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
public class EndUser extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numberOfPositivePoints;
    private int numberOfNegativePoints;

    @ManyToOne
    @JoinColumn(
            name = "statusId"
    )
    private UserStatus userStatus;
}