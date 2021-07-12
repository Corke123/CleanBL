CREATE TABLE ReportLog (
    id BIGINT AUTO_INCREMENT,
    description VARCHAR(255) NOT NULL,
    changedAt DATETIME NOT NULL,
    userId BIGINT NOT NULL,
    reportId BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_ReportLog_User FOREIGN KEY (userId)
        REFERENCES User (id),
    CONSTRAINT FK_ReportLog_Report FOREIGN KEY (reportId)
        REFERENCES Report (id)
);
