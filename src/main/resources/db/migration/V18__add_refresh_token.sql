CREATE TABLE IF NOT EXISTS `clean_bl`.`RefreshToken` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `createdDate` DATETIME NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;