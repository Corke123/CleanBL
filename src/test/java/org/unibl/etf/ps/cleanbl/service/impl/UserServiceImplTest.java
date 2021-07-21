package org.unibl.etf.ps.cleanbl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;
import org.unibl.etf.ps.cleanbl.service.EmailService;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.unibl.etf.ps.cleanbl.fixtures.UserSpringFixture.*;

class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCurrentlyLoggedInUser_should_return_user() {
        org.springframework.security.core.userdetails.User userSpring =
                (org.springframework.security.core.userdetails.User) crateUserSpring().build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userSpring);

        assertEquals(userSpring, userService.getCurrentlyLoggedInUser());
    }

    @Test
    void isLoggedInUserDepartmentOfficer_should_return_true_if_user_department_officer_is_logged() {
        org.springframework.security.core.userdetails.User userSpring =
                (org.springframework.security.core.userdetails.User) crateUserSpring().build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userSpring);

        assertTrue(userSpring.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_DepartmentOfficer")));
        assertTrue(userService.isLoggedInUserDepartmentOfficer());
    }

    @Test
    void getAuthorities_should_return_a_list_of_authorities() {
        org.springframework.security.core.userdetails.User userSpring =
                (org.springframework.security.core.userdetails.User) crateUserSpring().build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userSpring);
        List<String> stringList = Collections.singletonList("ROLE_DepartmentOfficer");

        assertEquals(stringList, userService.getAuthorities());
    }

    @Test
    void getUserByUsername_should_return_user() {
        User user = createUser().build();
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertEquals(user, userService.getUserByUsername(user.getUsername()));
    }

    @Test
    void getUserByUsername_should_throws_exception_when_not_exist_user_with_that_username() {
        String username = "username123";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userService.getUserByUsername(username));
    }

    @Test
    void getDepartmentOfficers_should_return_all_department_officers() {
        Role role = createRole().build();
        List<User> users = Collections.singletonList(
                createUser().build());

        when(roleRepository.findByName(role.getName())).thenReturn(Optional.of(role));
        when(userRepository.findAllByRoles(role)).thenReturn(users);

        assertEquals(users, userService.getDepartmentOfficers());
    }

    @Test
    void getDepartmentOfficers_should_throws_exception_when_role_department_officer_not_exist() {
        when(roleRepository.findByName("DepartmentOfficer")).thenThrow(RecordNotFoundException.class);
        assertThrows(RecordNotFoundException.class, () -> userService.getDepartmentOfficers());
    }

    @Test
    void getById_should_return_user_with_the_given_id() {
        User user = createUser().build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertTrue(userService.getById(user.getId()).isPresent());
        assertEquals(user, userService.getById(user.getId()).get());
    }

    @Test
    void save_should_return_saved_user() {
        User user = createUser().build();
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(user, userService.save(user));
    }

    @Test
    void addDepartmentOfficer_should_return_saved_department_officer() {
        User user = createUser().build();
        String password = "marko123";

        when(userRepository.save(user)).thenReturn(user);

        assertEquals(user, userService.addDepartmentOfficer(user, password));
        verify(emailService, times(1)).sendMessage(user.getEmail(),
                "Kreiranje naloga",
                "Kredencijali za pristup sistemu su korisničko ime: " + user.getUsername()
                        + ", lozinka: " + password);
    }

    @Test
    void deleteDepartmentOfficer_should_delete_department_officer_with_the_given_id() {
        User user = createUser().build();

        doNothing().when(userRepository).deleteById(user.getId());

        userService.deleteDepartmentOfficer(user.getId());
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void existsByEmail_should_return_true_if_exists_user_with_the_given_email() {
        User user = createUser().build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertTrue(userService.existsByEmail(user.getEmail()));
    }

    @Test
    void existsByUsername_should_return_true_if_exists_user_with_the_given_username() {
        User user = createUser().build();

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertTrue(userService.existsByUsername(user.getUsername()));
    }
}