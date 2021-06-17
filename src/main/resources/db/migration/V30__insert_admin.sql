-- Insert admin to database password is bcrypt hashed 'admin123'

INSERT INTO User (`username`, `firstName`, `lastName`, `email`, `password`, `statusId`)
    VALUES ('admin123', 'Marko', 'Markovic', 'marko@mail.com', '$2a$10$rFCU6/ZzGikS2xJakFSF.OvdmQRNSddnjpcMmAgLg0Lf4IbDHpVgm', '2');

INSERT INTO UserHasRoles(`userId`, `roleId`) VALUES (1, 1);