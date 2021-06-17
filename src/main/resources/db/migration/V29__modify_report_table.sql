ALTER TABLE Report DROP FOREIGN KEY fk_Report_EndUser1;
ALTER TABLE Report CHANGE COLUMN endUserId userId BIGINT(20) NOT NULL ;
ALTER TABLE Report
ADD CONSTRAINT fk_Report_User1
  FOREIGN KEY (userId)
  REFERENCES User (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE Report CHANGE COLUMN reportStatusId statusId BIGINT(20) NOT NULL ;

ALTER TABLE Report DROP FOREIGN KEY fk_Report_ReportStatus1;

ALTER TABLE Report ADD CONSTRAINT fk_Report_ReportStatus
  FOREIGN KEY (statusId)
  REFERENCES ReportStatus (id)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE Report ADD departmentServiceId BIGINT AFTER departmentId;
ALTER TABLE Report
    ADD CONSTRAINT fk_Report_DepartmentService
    FOREIGN KEY (departmentServiceId)
    REFERENCES DepartmentService(id);
