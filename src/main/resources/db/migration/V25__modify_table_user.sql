ALTER TABLE User DROP COLUMN created;
ALTER TABLE User ADD departmentId BIGINT NULL;
ALTER TABLE User ADD CONSTRAINT fk_User_Department FOREIGN KEY (departmentId) REFERENCES Department(id);

