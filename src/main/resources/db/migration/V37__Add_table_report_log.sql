create table clean_bl.reportlog
(
    id BIGINT AUTO_INCREMENT,
    dateAndTime DATETIME NOT NULL,
    userId BIGINT NOT NULL,
    reportId BIGINT NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT FK_reportlog_user
    FOREIGN KEY (userId)
    REFERENCES clean_bl.user(id),
    CONSTRAINT FK_reportlog_report
    FOREIGN KEY (reportId)
    REFERENCES clean_bl.report(id)
);
