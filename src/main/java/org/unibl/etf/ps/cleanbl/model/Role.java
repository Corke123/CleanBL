package org.unibl.etf.ps.cleanbl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "RoleHasPermissions",
            joinColumns = @JoinColumn(
                    name = "roleId", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "permissionId", referencedColumnName = "id"
            )
    )
    private List<Permission> permissions;
}
