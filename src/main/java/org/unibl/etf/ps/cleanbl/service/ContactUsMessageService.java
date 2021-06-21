package org.unibl.etf.ps.cleanbl.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;
import org.unibl.etf.ps.cleanbl.repository.ContactUsMessageRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ContactUsMessageService {
    private final ContactUsMessageRepository repository;
    private final EmailService emailService;

    public Page<ContactUsMessage> getAllFiltered(PageRequest pageRequest, String title, boolean allMessages) {
        log.info("Getting contact us messages by: pageSize = " + pageRequest.getPageSize() +
                ", page = " + pageRequest.getPageNumber() +
                ", sort = " + pageRequest.getSort() +
                ", title = " + title +
                ", allMessages = " + allMessages);
        if (isEmpty(title) && allMessages) {
            return repository.findAll(pageRequest);
        } else if (allMessages) {
            return repository.findAllByTitleContaining(pageRequest, title);
        } else if (isEmpty(title)) {
            return repository.findAllByReplied(pageRequest, false);
        } else {
            return repository.findAllByTitleContainingAndReplied(pageRequest, title, false);
        }
    }

    public ContactUsMessage add(ContactUsMessage contactUsMessage) {
        log.info("Add contact us message with title: " + contactUsMessage.getTitle());
        return repository.save(contactUsMessage);
    }

    @Transactional
    public Optional<ContactUsMessage> reply(Long id, String replyMessage) {
        Optional<ContactUsMessage> contactUsMessageOptional = repository.findById(id);
        if (contactUsMessageOptional.isPresent()) {
            ContactUsMessage contactUsMessage = contactUsMessageOptional.get();
            contactUsMessage.setResponse(replyMessage);
            contactUsMessage.setReplied(true);
            emailService.sendMessage(contactUsMessage.getEmail(),
                    contactUsMessage.getTitle(),
                    contactUsMessage.getResponse());
            return Optional.of(repository.save(contactUsMessage));
        } else {
            return Optional.empty();
        }
    }

    private boolean isEmpty(String searchString) {
        return searchString == null || searchString.isBlank();
    }

}
