package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    org.springframework.security.core.userdetails.User getCurrentlyLoggedInUser();
    boolean isLoggedInUserDepartmentOfficer();
    List<String> getAuthorities();
    User getUserByUsername(String username);
    List<User> getDepartmentOfficers();
    Optional<User> getById(Long id);
    User save(User user);
    User addDepartmentOfficer(User user, String password);
    void deleteDepartmentOfficer(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
