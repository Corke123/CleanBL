package org.unibl.etf.ps.cleanbl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final EmailService emailService;

    public org.springframework.security.core.userdetails.User getCurrentlyLoggedInUser() {
        return (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean isLoggedInUserDepartmentOfficer() {
        return getCurrentlyLoggedInUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_DepartmentOfficer"));
    }

    public List<String> getAuthorities() {
        return getCurrentlyLoggedInUser().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(str -> str.startsWith("ROLE_"))
                .collect(Collectors.toList());
    }

    public User getUserByUsername(String username) {
        log.info("Finding user by username: " + username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("There is no user with username: " + username));
    }

    public List<User> getDepartmentOfficers() {
        log.info("Getting department officers");
        return userRepository.findAllByRoles(roleRepository.findByName("DepartmentOfficer")
                .orElseThrow(() -> new RecordNotFoundException("There is no role department officer")));
    }

    public Optional<User> getById(Long id) {
        log.info("Getting user with id: " + id);
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User addDepartmentOfficer(User user, String password) {
        log.info("Creating account for department officer with username: " + user.getUsername());
        User result = save(user);
        emailService.sendMessage(
                user.getEmail(),
                "Kreiranje naloga",
                "Kredencijali za pristup sistemu su korisničko ime: " + user.getUsername() + ", lozinka: " + password);
        return result;
    }

    public void deleteDepartmentOfficer(Long id) {
        log.info("Deleting department officer with id: " + id);
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
