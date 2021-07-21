package org.unibl.etf.ps.cleanbl.service.impl;

import org.springframework.stereotype.Service;
import org.unibl.etf.ps.cleanbl.service.PasswordGeneratorService;

import java.util.Random;

@Service
public class PasswordGeneratorServiceImpl implements PasswordGeneratorService {
    @Override
    public String generateRandomPassword() {
        int passwordLength = 10;
        Random rand = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

        StringBuilder sb = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++) {
            sb.append(characters.charAt((int) (characters.length() * rand.nextDouble())));
        }

        return sb.toString();
    }
}
