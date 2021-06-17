ALTER TABLE Comment DROP FOREIGN KEY fk_Comment_EndUser;
ALTER TABLE Comment ADD CONSTRAINT fk_Comment_User FOREIGN KEY (userId) REFERENCES User(id);
ALTER TABLE Comment CHANGE COLUMN dateOfPublication createdAt DATE NOT NULL ;
