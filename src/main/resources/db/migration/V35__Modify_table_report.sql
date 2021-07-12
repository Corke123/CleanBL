alter table Report drop foreign key fk_Report_Street1;
alter table Report drop column streetId;
alter table Report add column title VARCHAR(255) COLLATE utf8_unicode_ci NOT NULL;
alter table Report add column latitude DECIMAL(10,6) NOT NULL;
alter table Report add column longitude DECIMAL(10,6) NOT NULL;
