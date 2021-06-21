package org.unibl.etf.ps.cleanbl.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.ps.cleanbl.dto.ReplyRequest;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;
import org.unibl.etf.ps.cleanbl.service.ContactUsMessageService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/contact-us-messages")
@AllArgsConstructor
public class ContactUsMessageController {

    private final ContactUsMessageService contactUsMessageService;

    private static final String PAGE = "0";
    private static final String SIZE = "10";

    @GetMapping
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<Page<ContactUsMessage>> getFiltered(
            @RequestParam(value = "page", defaultValue = PAGE) Integer page,
            @RequestParam(value = "size", defaultValue = SIZE) Integer size,
            @RequestParam(value = "sort", defaultValue = "title") String sortColumn,
            @RequestParam(value = "order", defaultValue = "DESC") String sortOrder,
            @RequestParam(value = "searchString", defaultValue = "") String searchString,
            @RequestParam(value = "all", defaultValue = "false") Boolean all) {
        return ResponseEntity.ok(
                contactUsMessageService.getAllFiltered(
                        PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortColumn)),
                        searchString, all)
        );
    }

    @PostMapping
    public ResponseEntity<ContactUsMessage> addContactUsMessage(@Valid @RequestBody ContactUsMessage contactUsMessage) {
        ContactUsMessage saved = contactUsMessageService.add(contactUsMessage);
        return ResponseEntity.created(URI.create("/api/v1/reports/" + saved.getId()))
                .body(saved);
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public ResponseEntity<ContactUsMessage> replyToMessage(@PathVariable("id") Long id,
                                                           @Valid @RequestBody ReplyRequest replyRequest) {
        return contactUsMessageService.reply(id, replyRequest.getReplyMessage())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
