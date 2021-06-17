package org.unibl.etf.ps.cleanbl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long reportId;
    private LocalDate createdAt;
    private String content;
    private String username;
}
