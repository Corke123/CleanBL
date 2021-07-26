package org.unibl.etf.ps.cleanbl.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;
import org.unibl.etf.ps.cleanbl.repository.ContactUsMessageRepository;
import org.unibl.etf.ps.cleanbl.service.EmailService;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.unibl.etf.ps.cleanbl.fixtures.ContactUsMessageFixture.createContactUsMessage;
import static org.unibl.etf.ps.cleanbl.fixtures.ContactUsMessageFixture.createContactUsMessageOne;


class ContactUsMessageServiceImplTest {

    @InjectMocks
    ContactUsMessageServiceImpl contactUsMessageService;

    @Mock
    ContactUsMessageRepository repository;

    @Mock
    EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;
    private static final String SORT_BY_CREATION_DATE = "createdAt";
    private static final Sort SORT = Sort.by(Sort.Direction.DESC, SORT_BY_CREATION_DATE);
    private static final PageRequest PAGE_REQUEST = PageRequest.of(PAGE_NUMBER, PAGE_SIZE, SORT);

    @Test
    void getAllFiltered_find_all() {
        ContactUsMessage contactUsMessage = createContactUsMessage();
        ContactUsMessage contactUsMessageOne = createContactUsMessageOne();
        PageRequest pageRequest = PAGE_REQUEST;
        Page<ContactUsMessage> expectedPage =
                new PageImpl<>(Arrays.asList(contactUsMessage, contactUsMessageOne));

        when(repository.findAll(pageRequest)).thenReturn(expectedPage);

        Page<ContactUsMessage> contactUsMessagePage =
                contactUsMessageService.getAllFiltered(pageRequest, "", true);

        assertEquals(expectedPage, contactUsMessagePage);
    }

    @Test
    void getAllFiltered_find_all_by_title_containing() {
        ContactUsMessage contactUsMessage = createContactUsMessage();
        ContactUsMessage contactUsMessageOne = createContactUsMessageOne();
        PageRequest pageRequest = PAGE_REQUEST;
        Page<ContactUsMessage> expectedPage =
                new PageImpl<>(Arrays.asList(contactUsMessage, contactUsMessageOne));

        when(repository.findAllByTitleContaining(pageRequest, contactUsMessage.getTitle())).thenReturn(expectedPage);

        Page<ContactUsMessage> contactUsMessagePage =
                contactUsMessageService.getAllFiltered(pageRequest, "Registracija na sistem", true);

        assertEquals(expectedPage, contactUsMessagePage);
    }

    @Test
    void getAllFiltered_find_all_by_replied() {
        ContactUsMessage contactUsMessage = createContactUsMessage();
        ContactUsMessage contactUsMessageOne = createContactUsMessageOne();
        PageRequest pageRequest = PAGE_REQUEST;
        Page<ContactUsMessage> expectedPage =
                new PageImpl<>(Arrays.asList(contactUsMessage, contactUsMessageOne));

        when(repository.findAllByReplied(pageRequest, false)).thenReturn(expectedPage);

        Page<ContactUsMessage> contactUsMessagePage =
                contactUsMessageService.getAllFiltered(pageRequest, "", false);

        assertEquals(expectedPage, contactUsMessagePage);
    }

    @Test
    void getAllFiltered_find_all_by_title_containing_and_replied() {
        ContactUsMessage contactUsMessage = createContactUsMessage();
        ContactUsMessage contactUsMessageOne = createContactUsMessageOne();
        PageRequest pageRequest = PAGE_REQUEST;
        Page<ContactUsMessage> expectedPage =
                new PageImpl<>(Arrays.asList(contactUsMessage, contactUsMessageOne));

        when(repository.findAllByTitleContainingAndReplied(pageRequest, contactUsMessage.getTitle(), false))
                .thenReturn(expectedPage);

        Page<ContactUsMessage> contactUsMessagePage =
                contactUsMessageService.getAllFiltered(pageRequest, "Registracija na sistem", false);

        assertEquals(expectedPage, contactUsMessagePage);
    }

    @Test
    void add_should_return_contact_us_message() {
        ContactUsMessage contactUsMessage = createContactUsMessage();

        when(repository.save(contactUsMessage)).thenReturn(contactUsMessage);

        assertEquals(contactUsMessage,contactUsMessageService.add(contactUsMessage));
    }

    @Test
    void reply_should_return_contact_us_message() {
        Long id = 1L;
        String replyMessage = "Nalog sa datim email-om vec postoji";
        ContactUsMessage contactUsMessage = createContactUsMessage();

        when(repository.findById(id)).thenReturn(Optional.of(contactUsMessage));
        when(repository.save(contactUsMessage)).thenReturn(contactUsMessage);

        assertTrue(contactUsMessageService.reply(id, replyMessage).isPresent());
        assertEquals(contactUsMessage, contactUsMessageService.reply(id, replyMessage).get());
        verify(emailService, times(2)).sendMessage(contactUsMessage.getEmail(),
                contactUsMessage.getTitle(),
                contactUsMessage.getResponse());
    }

    @Test
    void reply_should_return_empty() {
        Long id = 1L;
        String replyMessage = "Nalog sa datim email-om vec postoji";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertFalse(contactUsMessageService.reply(id, replyMessage).isPresent());
    }
}
