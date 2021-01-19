package org.unibl.etf.ps.cleanbl.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.model.Permission;
import org.unibl.etf.ps.cleanbl.model.Role;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("There is no user with username: " + username));
        UserStatus userStatus = userStatusRepository.findByName("active").orElseThrow(() -> new RecordNotFoundException("There is no user status with name: active"));

        boolean enabled = user.getUserStatus().equals(userStatus);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), enabled,
                true, true, true,
                getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Set<SimpleGrantedAuthority> authoritySet = new HashSet<>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                authoritySet.add(new SimpleGrantedAuthority(permission.getName()));
            }
            authoritySet.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authoritySet;
    }
}
