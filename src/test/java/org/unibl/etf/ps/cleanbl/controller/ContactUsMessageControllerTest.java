package org.unibl.etf.ps.cleanbl.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.unibl.etf.ps.cleanbl.dto.ReplyRequest;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;
import org.unibl.etf.ps.cleanbl.service.ContactUsMessageService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.unibl.etf.ps.cleanbl.fixtures.ContactUsMessageFixture.createContactUsMessage;
import static org.unibl.etf.ps.cleanbl.fixtures.ContactUsMessageFixture.createContactUsMessageOne;

class ContactUsMessageControllerTest {
    @InjectMocks
    ContactUsMessageController contactUsMessageController;

    @Mock
    ContactUsMessageService contactUsMessageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFiltered_should_return_page_contact_us_message() {
     ContactUsMessage contactUsMessage = createContactUsMessage();
     ContactUsMessage contactUsMessageOne = createContactUsMessageOne();
     Page<ContactUsMessage> expectedPage =
             new PageImpl<>(Arrays.asList(contactUsMessage, contactUsMessageOne));

     when(contactUsMessageService.
             getAllFiltered(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "title")),
             "Registracija na sistem",
             true))
             .thenReturn(expectedPage);

     ResponseEntity<Page<ContactUsMessage>> responseEntity =
             contactUsMessageController.getFiltered(0,
                     10,
                     "title",
                     "DESC",
                     "Registracija na sistem",
                     true);
     assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
     assertEquals(expectedPage, responseEntity.getBody());
    }

    @Test
    void addContactUsMessage_should_return_contact_us_message() {
        ContactUsMessage contactUsMessage = createContactUsMessage();

        when(contactUsMessageService.add(contactUsMessage)).thenReturn(contactUsMessage);

        ResponseEntity<ContactUsMessage> responseEntity =
                contactUsMessageController.addContactUsMessage(contactUsMessage);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(contactUsMessage, responseEntity.getBody());
    }

    @Test
    void replyToMessage_should_return_contact_us_message() {
        ContactUsMessage contactUsMessage = createContactUsMessage();
        Long id = 1L;
        ReplyRequest replyRequest= new ReplyRequest("Nalog sa datim email-om vec postoji");

        when(contactUsMessageService.reply(id, replyRequest.getReplyMessage())).thenReturn(Optional.of(contactUsMessage));

        ResponseEntity<ContactUsMessage> responseEntity =
                contactUsMessageController.replyToMessage(id, replyRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(contactUsMessage, responseEntity.getBody());
    }

    @Test
    void replyToMessage_should_return_response_entity_status_not_found() {
        Long id = 1L;
        ReplyRequest replyRequest= new ReplyRequest("Nalog sa datim email-om vec postoji");

        when(contactUsMessageService.reply(id, replyRequest.getReplyMessage())).thenReturn(Optional.empty());

        ResponseEntity<ContactUsMessage> responseEntity =
                contactUsMessageController.replyToMessage(id, replyRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}
