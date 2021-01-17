-- -----------------------------------------------------
-- Table `clean_bl`.`Role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `clean_bl`.`Permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `clean_bl`.`UserHasRoles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`UserHasRoles` (
  `userId` BIGINT NOT NULL,
  `roleId` BIGINT NOT NULL,
  PRIMARY KEY (`userId`, `roleId`),
  INDEX `fk_User_has_Role_Role1_idx` (`roleId` ASC),
  INDEX `fk_User_has_Role_User1_idx` (`userId` ASC),
  CONSTRAINT `fk_User_has_Role_User1`
    FOREIGN KEY (`userId`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_User_has_Role_Role1`
    FOREIGN KEY (`roleId`)
    REFERENCES `clean_bl`.`Role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `clean_bl`.`RoleHasPermissions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`RoleHasPermissions` (
  `permissionId` BIGINT NOT NULL,
  `roleId` BIGINT NOT NULL,
  PRIMARY KEY (`permissionId`, `roleId`),
  INDEX `fk_Permission_has_Role_Role1_idx` (`roleId` ASC),
  INDEX `fk_Permission_has_Role_Permission1_idx` (`permissionId` ASC),
  CONSTRAINT `fk_Permission_has_Role_Permission1`
    FOREIGN KEY (`permissionId`)
    REFERENCES `clean_bl`.`Permission` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Permission_has_Role_Role1`
    FOREIGN KEY (`roleId`)
    REFERENCES `clean_bl`.`Role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;