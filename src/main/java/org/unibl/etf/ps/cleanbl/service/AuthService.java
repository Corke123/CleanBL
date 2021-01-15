package org.unibl.etf.ps.cleanbl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.exception.EmailTakenException;
import org.unibl.etf.ps.cleanbl.exception.UsernameTakenException;
import org.unibl.etf.ps.cleanbl.model.EndUser;
import org.unibl.etf.ps.cleanbl.model.User;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.model.VerificationToken;
import org.unibl.etf.ps.cleanbl.repository.UserRepository;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.repository.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    private static final String SUBJECT_MESSAGE = "Please activate your account";
    private static final String VERIFICATION_LINK = "http://localhost:8080/api/accountVerification/";
    private static final String GENERIC_MESSAGE = "Thank you for signing up to CleanBL, please click on below link to activate your account: " + VERIFICATION_LINK;

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
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            log.info("Attempt to register with taken email: " + registerRequest.getEmail());
            throw new EmailTakenException("Email is taken");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            log.info("Attempt to register with taken username: " + registerRequest.getUsername());
            throw new UsernameTakenException("Username is taken");
        }

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

        emailService.sendMessage(registerRequest.getEmail(), SUBJECT_MESSAGE, GENERIC_MESSAGE + token);
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
