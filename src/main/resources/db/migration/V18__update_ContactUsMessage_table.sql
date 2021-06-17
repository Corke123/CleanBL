ALTER TABLE ContactUsMessage ADD title VARCHAR(255) NOT NULL AFTER id;
ALTER TABLE ContactUsMessage ADD email VARCHAR(255) NOT NULL AFTER content;
ALTER TABLE ContactUsMessage CHANGE COLUMN seen replied TINYINT(1) NULL DEFAULT 0 ;
ALTER TABLE ContactUsMessage DROP FOREIGN KEY fk_ContactUsMessage_EndUser;
ALTER TABLE ContactUsMessage DROP COLUMN userId;