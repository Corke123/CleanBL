package org.unibl.etf.ps.cleanbl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.AuthenticationResponse;
import org.unibl.etf.ps.cleanbl.dto.LoginRequest;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.exception.EmailTakenException;
import org.unibl.etf.ps.cleanbl.exception.RecordNotFoundException;
import org.unibl.etf.ps.cleanbl.exception.UsernameTakenException;
import org.unibl.etf.ps.cleanbl.exception.VerificationTokenException;
import org.unibl.etf.ps.cleanbl.mapper.UserMapper;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.model.VerificationToken;
import org.unibl.etf.ps.cleanbl.repository.RoleRepository;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.repository.VerificationTokenRepository;
import org.unibl.etf.ps.cleanbl.security.JwtProvider;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private static final String SUBJECT_MESSAGE = "Please activate your account";
    private static final String VERIFICATION_LINK = "http://localhost:8080/api/v1/auth/accountVerification/";
    private static final String GENERIC_MESSAGE = "Thank you for signing up to CleanBL, please click on below link to activate your account: "
            + VERIFICATION_LINK;
    private final UserService userService;
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        log.info("Creating new account for user: " + registerRequest.getFirstName() + " " + registerRequest.getLastName());
        checkIfUserExists(registerRequest);

        User user = userMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setUserStatus(userStatusRepository.findByName("inactive")
                .orElseThrow(() -> new RecordNotFoundException("There is no status inactive")));
        user.setRoles(Collections.singletonList(roleRepository.findByName("User")
                .orElseThrow(() -> new RecordNotFoundException("There is no role user"))));
        userRepository.save(user);

        String token = generateVerificationToken(user);
        emailService.sendMessage(registerRequest.getEmail(), SUBJECT_MESSAGE, GENERIC_MESSAGE + token);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    public void verifyAccount(String token) {
        fetchUserAndEnable(verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenException("Invalid verification token")));
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(verificationToken.getUser().getUsername())
                .orElseThrow(() -> new VerificationTokenException("User not found with username: " + username));
        UserStatus activeStatus = userStatusRepository.findByName("active")
                .orElseThrow(() -> new RecordNotFoundException("Unknown user status"));
        user.setUserStatus(activeStatus);
        userRepository.save(user);
        log.info("User with id " + user.getId() + " activated his account");
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        log.info("Attempt to login with username: " + loginRequest.getUsername());
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(
                token,
                Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()),
                loginRequest.getUsername(),
                userService.getAuthorities());
    }

    private void checkIfUserExists(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.info("Attempt to register with taken email: " + registerRequest.getEmail());
            throw new EmailTakenException("Email is taken");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.info("Attempt to register with taken username: " + registerRequest.getUsername());
            throw new UsernameTakenException("Username is taken");
        }
    }
}
