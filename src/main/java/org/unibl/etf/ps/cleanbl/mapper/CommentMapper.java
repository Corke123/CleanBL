package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.CommentDTO;
import org.unibl.etf.ps.cleanbl.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "reportId", source = "report.id")
    @Mapping(target = "username", source = "endUser.username")
    CommentDTO toDTO(Comment comment);
}
