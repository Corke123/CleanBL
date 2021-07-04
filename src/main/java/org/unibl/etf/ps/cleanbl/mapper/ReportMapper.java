package org.unibl.etf.ps.cleanbl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.unibl.etf.ps.cleanbl.dto.ReportResponse;
import org.unibl.etf.ps.cleanbl.model.Report;

@Mapper(componentModel = "spring")
public interface ReportMapper {
    @Mapping(target = "userReported", source = "report.user.username")
    @Mapping(target = "status", source = "report.reportStatus.name")
    @Mapping(target = "street", source = "report.street.name")
    @Mapping(target = "partOfTheCity", source = "report.street.partOfTheCity.name")
    @Mapping(target = "department", source = "report.department.name")
    @Mapping(target = "departmentService", source = "report.departmentService.name")
    @Mapping(target = "valid", source = "valid")
    ReportResponse reportToReportResponse(Report report);
}
