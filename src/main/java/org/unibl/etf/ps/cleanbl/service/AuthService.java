package org.unibl.etf.ps.cleanbl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.model.EndUser;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.model.VerificationToken;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.repository.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserStatusRepository userStatusRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository,
                       EmailService emailService) {
        this.userStatusRepository = userStatusRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        EndUser endUser = new EndUser();
        endUser.setFirstName(registerRequest.getFirstName());
        endUser.setLastName(registerRequest.getLastName());
        endUser.setUsername(registerRequest.getUsername());
        endUser.setEmail(registerRequest.getEmail());
        endUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        endUser.setNumberOfNegativePoints(0);
        endUser.setNumberOfPositivePoints(0);
        Optional<UserStatus> userStatusOptional = userStatusRepository.findByName("inactive");
        endUser.setUserStatus(userStatusOptional.orElseThrow(RuntimeException::new));

        userRepository.save(endUser);

        String token = generateVerificationToken(endUser);

        emailService.sendMessage(registerRequest.getEmail(), "Please activate your account", "Thank you for signing up to CleanBL, please click on below link" +
                "to activate your account: " + "http://localhost:8080/api/accountVerification/" + token);
    }

    private String generateVerificationToken(EndUser endUser) {
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(endUser);

        verificationTokenRepository.save(verificationToken);

        return token;
    }
}
