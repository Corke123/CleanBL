package org.unibl.etf.ps.cleanbl.service;

import java.util.Map;

public interface EmailService {
    void sendMessage(String to, String subject, String text);
    void sendMessageUsingThymeleafTemplate(String to, String subject, Map<String, Object> templateModel);
}
