create table User (
    id bigint not null auto_increment,
    username varchar(20) collate utf8_unicode_ci not null,
    firstName varchar(20) not null,
    lastName varchar(20) not null,
    email varchar(50) not null,
    password varchar(50) not null,
    primary key (id),

    constraint UK_user_username unique (username),
    constraint UK_user_email unique (email)
);

create table user_sequence (
    next_val bigint
);