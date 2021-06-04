package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.model.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "userReported", source = "report.endUser.username")
    @Mapping(target = "status", source = "report.reportStatus.name")
    @Mapping(target = "street", source = "report.street.name")
    @Mapping(target = "partOfTheCity", source = "report.street.partOfTheCity.name")
    @Mapping(target = "department", source = "report.department.name")
    ReportResponse reportToReportResponse(Report report);
}
