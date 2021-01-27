package org.unibl.etf.ps.cleanbl.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;

@Service
@Slf4j
public class JwtProvider {

    @Value("${app.security.jwt.keystore-location}")
    private String keyStorePath;

    @Value("${app.security.jwt.keystore-password}")
    private String keyStorePassword;

    @Value("${app.security.jwt.key-alias}")
    private String keyAlias;

    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("pkcs12");
            InputStream resourceAsStream = getClass().getResourceAsStream(keyStorePath);
            keyStore.load(resourceAsStream, keyStorePassword.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            log.info("Unable to read keystore");
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withSubject(principal.getUsername())
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            log.info("Invalid Signing configuration / Couldn't convert Claims.");
        }
        return null;
    }

    public String generateTokenWithUsername(String username) {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            return JWT.create()
                    .withSubject(username)
                    .withIssuedAt(Date.from(Instant.now()))
                    .withExpiresAt(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            log.info("Invalid Signing configuration / Couldn't convert Claims.");
        }
        return null;
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.info("Exception while loading private key from keystore");
        }
        return null;
    }

    public boolean validateToken(String token) {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            log.info("Invalid signature/claims");
            throw exception;
        }
    }

    private PublicKey getPublicKey() {
        try {
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (KeyStoreException e) {
            log.info("Unable to retrieve public key from keystore");
        }
        return null;
    }

    public String getUsernameFromJwt(String token) {
        RSAPublicKey publicKey = (RSAPublicKey) getPublicKey();
        RSAPrivateKey privateKey = (RSAPrivateKey) getPrivateKey();
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            log.info("Invalid signature/claims");
            throw exception;
        }
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}
