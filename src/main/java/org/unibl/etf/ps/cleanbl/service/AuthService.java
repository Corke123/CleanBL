package org.unibl.etf.ps.cleanbl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;
import org.unibl.etf.ps.cleanbl.exception.EmailTakenException;
import org.unibl.etf.ps.cleanbl.exception.UnknownUserStatusException;
import org.unibl.etf.ps.cleanbl.exception.UsernameTakenException;
import org.unibl.etf.ps.cleanbl.exception.VerificationTokenException;
import org.unibl.etf.ps.cleanbl.model.EndUser;
import org.unibl.etf.ps.cleanbl.model.UserStatus;
import org.unibl.etf.ps.cleanbl.model.VerificationToken;
import org.unibl.etf.ps.cleanbl.repository.EndUserRepository;
import org.unibl.etf.ps.cleanbl.repository.UserStatusRepository;
import org.unibl.etf.ps.cleanbl.repository.VerificationTokenRepository;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthService {

    private final UserStatusRepository userStatusRepository;
    private final EndUserRepository endUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final EmailService emailService;

    private static final String SUBJECT_MESSAGE = "Please activate your account";
    private static final String VERIFICATION_LINK = "http://localhost:8080/api/auth/accountVerification/";
    private static final String GENERIC_MESSAGE = "Thank you for signing up to CleanBL, please click on below link to activate your account: " + VERIFICATION_LINK;

    @Autowired
    public AuthService(UserStatusRepository userStatusRepository,
                       EndUserRepository endUserRepository,
                       PasswordEncoder passwordEncoder,
                       VerificationTokenRepository verificationTokenRepository,
                       EmailService emailService) {
        this.userStatusRepository = userStatusRepository;
        this.endUserRepository = endUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void signup(RegisterRequest registerRequest) {
        if (endUserRepository.existsByEmail(registerRequest.getEmail())) {
            log.info("Attempt to register with taken email: " + registerRequest.getEmail());
            throw new EmailTakenException("Email is taken");
        }

        if (endUserRepository.existsByUsername(registerRequest.getUsername())) {
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

        endUserRepository.save(endUser);

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

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new VerificationTokenException("Invalid token"));
        fetchUserAndEnable(verificationToken.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        EndUser user = endUserRepository.findByUsername(username).orElseThrow(() -> new VerificationTokenException("User not found with username: " + username));
        UserStatus activateStatus = userStatusRepository.findByName("active").orElseThrow(() -> new UnknownUserStatusException("Unknown user status"));
        user.setUserStatus(activateStatus);
        endUserRepository.save(user);
    }
}
