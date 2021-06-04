package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.EndUser;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentlyLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isLoggedInUserDepartmentOfficer() {
        return getCurrentlyLoggedInUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_DepartmentOfficer"));
    }

    public EndUser getEndUserByUsername(String username) {
        return (EndUser) userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
    }

}
