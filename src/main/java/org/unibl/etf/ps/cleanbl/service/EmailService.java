package org.unibl.etf.ps.cleanbl.service;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
}
