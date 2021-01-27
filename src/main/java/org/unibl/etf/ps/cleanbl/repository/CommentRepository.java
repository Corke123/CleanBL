package org.unibl.etf.ps.cleanbl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.Comment;
import org.unibl.etf.ps.cleanbl.model.Report;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByReport(Report report);
}
