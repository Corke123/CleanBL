package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.ps.cleanbl.model.ContactUsMessage;

@Repository
public interface ContactUsMessageRepository extends JpaRepository<ContactUsMessage, Long> {
    Page<ContactUsMessage> findAllByTitleContainingAndReplied(Pageable pageable, String title, Boolean replied);

    Page<ContactUsMessage> findAllByReplied(Pageable pageable, Boolean replied);

    Page<ContactUsMessage> findAllByTitleContaining(Pageable pageable, String title);
}
