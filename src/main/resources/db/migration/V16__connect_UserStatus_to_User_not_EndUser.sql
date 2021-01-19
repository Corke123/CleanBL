-- Drop foreign key to UserStatus
ALTER TABLE `clean_bl`.`EndUser` DROP FOREIGN KEY `fk_EndUser_UserStatus`;
ALTER TABLE `clean_bl`.`EndUser` DROP `statusId`;

-- Add foreign key to UserStatus
ALTER TABLE `clean_bl`.`User`
  ADD COLUMN `statusId` BIGINT NOT NULL;

ALTER TABLE `clean_bl`.`User`
  ADD CONSTRAINT `fk_User_UserStatus`
  FOREIGN KEY (`statusId`)
  REFERENCES `clean_bl`.`UserStatus` (`id`);