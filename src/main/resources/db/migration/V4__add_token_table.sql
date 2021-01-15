-- -----------------------------------------------------
-- Table `clean_bl`.`Token`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Token` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL,
  `userId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Token_User_idx` (`userId` ASC),
    CONSTRAINT `fk_Token_User`
      FOREIGN KEY (`userId`)
      REFERENCES `clean_bl`.`User` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION)
ENGINE = InnoDB;