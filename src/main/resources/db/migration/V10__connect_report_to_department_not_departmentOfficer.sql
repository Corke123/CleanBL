-- Drop foreign key to DepartmentOfficer
ALTER TABLE `clean_bl`.`Report` DROP FOREIGN KEY `fk_Report_DepartmentOfficer1`;
ALTER TABLE `clean_bl`.`Report` DROP `departmentOfficerId`;

-- Add foreign key to Department
ALTER TABLE `clean_bl`.`Report`
  ADD COLUMN `departmentId` BIGINT NOT NULL;

ALTER TABLE `clean_bl`.`Report`
  ADD CONSTRAINT `fk_Report_Department`
  FOREIGN KEY (`departmentId`)
  REFERENCES `clean_bl`.`Department` (`id`);