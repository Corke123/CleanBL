package org.unibl.etf.ps.cleanbl.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;

import java.util.Optional;

public interface ContactUsMessageService {
    Page<ContactUsMessage> getAllFiltered(PageRequest pageRequest, String title, boolean allMessages);
    ContactUsMessage add(ContactUsMessage contactUsMessage);
    Optional<ContactUsMessage> reply(Long id, String replyMessage);
}
