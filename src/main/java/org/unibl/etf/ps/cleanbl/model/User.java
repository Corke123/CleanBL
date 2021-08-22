package org.unibl.etf.ps.cleanbl.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    @Email
    private String email;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "UserHasRoles",
            joinColumns = @JoinColumn(
                    name = "userId", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "id"
            )
    )

    private List<Role> roles;

    @ManyToOne
    @JoinColumn(
            name = "statusId"
    )

    private UserStatus userStatus;

    @ManyToOne
    @JoinColumn(
            name = "departmentId"
    )

    private Department department;
}
