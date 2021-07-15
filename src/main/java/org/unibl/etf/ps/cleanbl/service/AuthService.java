package org.unibl.etf.ps.cleanbl.service;

import org.unibl.etf.ps.cleanbl.dto.AuthenticationResponse;
import org.unibl.etf.ps.cleanbl.dto.LoginRequest;
import org.unibl.etf.ps.cleanbl.dto.RegisterRequest;

public interface AuthService {
    void signup(RegisterRequest registerRequest);
    void verifyAccount(String token);
    AuthenticationResponse login(LoginRequest loginRequest);
}
