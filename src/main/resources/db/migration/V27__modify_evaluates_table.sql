ALTER TABLE Evaluates DROP FOREIGN KEY fk_Evaluates_EndUser;

ALTER TABLE Evaluates CHANGE COLUMN endUserId userId BIGINT(20) NOT NULL ,
    ADD INDEX fk_Evaluates_User_idx (userId ASC),
    DROP INDEX fk_Evaluates_EndUser_idx;

ALTER TABLE Evaluates
    ADD CONSTRAINT fk_Evaluates_User
    FOREIGN KEY (userId)
    REFERENCES User (id);

ALTER TABLE Evaluates CHANGE COLUMN grade grade INT NOT NULL ;
