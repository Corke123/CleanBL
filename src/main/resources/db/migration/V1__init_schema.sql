-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema clean_bl
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema clean_bl
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `clean_bl` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
USE `clean_bl` ;

-- -----------------------------------------------------
-- Table `clean_bl`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`User` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `firstName` VARCHAR(255) NOT NULL,
  `lastName` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `created` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Department`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Administrator`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Administrator` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  INDEX `fk_Administrator_User_idx` (`id` ASC),
  CONSTRAINT `fk_Administrator_User`
    FOREIGN KEY (`id`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`DepartmentOfficer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`DepartmentOfficer` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `departmentId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_DepartmentOfficer_Department_idx` (`departmentId` ASC),
  CONSTRAINT `fk_DepartmentOfficer_User`
    FOREIGN KEY (`id`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DepartmentOfficer_Department`
    FOREIGN KEY (`departmentId`)
    REFERENCES `clean_bl`.`Department` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`PartOfTheCity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`PartOfTheCity` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Street`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Street` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `partOfTheCityId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Street_PartOfTheCity_idx` (`partOfTheCityId` ASC),
  CONSTRAINT `fk_Street_PartOfTheCity`
    FOREIGN KEY (`partOfTheCityId`)
    REFERENCES `clean_bl`.`PartOfTheCity` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`UserStatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`UserStatus` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`EndUser`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`EndUser` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `numberOfPositivePoints` INT NOT NULL DEFAULT 0,
  `numberOfNegativePoints` INT NOT NULL DEFAULT 0,
  `statusId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_EndUser_UserStatus_idx` (`statusId` ASC),
  CONSTRAINT `fk_EndUser_User`
    FOREIGN KEY (`id`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EndUser_UserStatus`
    FOREIGN KEY (`statusId`)
    REFERENCES `clean_bl`.`UserStatus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`ReportStatus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`ReportStatus` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Report`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Report` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `endUserId` BIGINT NOT NULL,
  `description` VARCHAR(100) NOT NULL,
  `image` BLOB NOT NULL,
  `date` DATE NOT NULL,
  `reportStatusId` BIGINT NOT NULL,
  `streetId` BIGINT NOT NULL,
  `departmentOfficerId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Report_EndUser_idx` (`endUserId` ASC),
  INDEX `fk_Report_ReportStatus_idx` (`reportStatusId` ASC),
  INDEX `fk_Report_Street_idx` (`streetId` ASC),
  INDEX `fk_Report_DepartmentOfficer_idx` (`departmentOfficerId` ASC),
  CONSTRAINT `fk_Report_EndUser1`
    FOREIGN KEY (`endUserId`)
    REFERENCES `clean_bl`.`EndUser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_ReportStatus1`
    FOREIGN KEY (`reportStatusId`)
    REFERENCES `clean_bl`.`ReportStatus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_Street1`
    FOREIGN KEY (`streetId`)
    REFERENCES `clean_bl`.`Street` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Report_DepartmentOfficer1`
    FOREIGN KEY (`departmentOfficerId`)
    REFERENCES `clean_bl`.`DepartmentOfficer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`DepartmentService`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`DepartmentService` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `phone` VARCHAR(20) NOT NULL,
  `departmentId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_DepartmentService_Department_idx` (`departmentId` ASC),
  CONSTRAINT `fk_DepartmentService_Department`
    FOREIGN KEY (`departmentId`)
    REFERENCES `clean_bl`.`Department` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Comment` (
  `id` BIGINT NOT NULL,
  `reportId` BIGINT NOT NULL,
  `dateOfPublication` DATETIME NOT NULL,
  `content` VARCHAR(1000) NOT NULL,
  `userId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Comment_Report_idx` (`reportId` ASC),
  INDEX `fk_Comment_EndUser_idx` (`userId` ASC),
  CONSTRAINT `fk_Comment_Report`
    FOREIGN KEY (`reportId`)
    REFERENCES `clean_bl`.`Report` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Comment_EndUser`
    FOREIGN KEY (`userId`)
    REFERENCES `clean_bl`.`EndUser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`Evaluates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`Evaluates` (
  `reportId` BIGINT NOT NULL,
  `endUserId` BIGINT NOT NULL,
  `departmentServiceId` BIGINT NOT NULL,
  `grade` DECIMAL(2) NULL,
  PRIMARY KEY (`reportId`, `endUserId`, `departmentServiceId`),
  INDEX `fk_Evaluates_Report_idx` (`reportId` ASC),
  INDEX `fk_Evaluates_EndUser_idx` (`endUserId` ASC),
  INDEX `fk_Evaluates_DepartmentService_idx` (`departmentServiceId` ASC),
  CONSTRAINT `fk_Evaluates_Report`
    FOREIGN KEY (`reportId`)
    REFERENCES `clean_bl`.`Report` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Evaluates_EndUser`
    FOREIGN KEY (`endUserId`)
    REFERENCES `clean_bl`.`EndUser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Evaluates_DepartmentService`
    FOREIGN KEY (`departmentServiceId`)
    REFERENCES `clean_bl`.`DepartmentService` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`SystemImprovement`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`SystemImprovement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(150) NOT NULL,
  `administratorId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_SystemImprovement_Administrator_idx` (`administratorId` ASC),
  CONSTRAINT `fk_SystemImprovement_Administrator`
    FOREIGN KEY (`administratorId`)
    REFERENCES `clean_bl`.`Administrator` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`ContactUsMessage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`ContactUsMessage` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(1000) NOT NULL,
  `read` TINYINT NOT NULL,
  `userId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ContactUsMessage_EndUser_idx` (`userId` ASC),
  CONSTRAINT `fk_ContactUsMessage_EndUser`
    FOREIGN KEY (`userId`)
    REFERENCES `clean_bl`.`EndUser` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `clean_bl`.`ChatMessage`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `clean_bl`.`ChatMessage` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `content` VARCHAR(1000) NOT NULL,
  `senderId` BIGINT NOT NULL,
  `receiverId` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ChatMessage_User1_idx` (`senderId` ASC),
  INDEX `fk_ChatMessage_User2_idx` (`receiverId` ASC),
  CONSTRAINT `fk_ChatMessage_User1`
    FOREIGN KEY (`senderId`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_ChatMessage_User2`
    FOREIGN KEY (`receiverId`)
    REFERENCES `clean_bl`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
