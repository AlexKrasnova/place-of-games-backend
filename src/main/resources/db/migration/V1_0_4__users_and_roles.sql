create table role_table
(
    name varchar(20) primary key not null
);

create table user_table
(
    login    varchar(50) primary key not null,
    password varchar(500),
    name     varchar(50)
);

create table user_role_link
(
    user_login varchar(50) not null,
    role_name  varchar(20) not null,
    primary key (user_login, role_name),
    foreign key (user_login) references user_table,
    foreign key (role_name) references role_table
);

insert into role_table (name) values ('ROLE_USER');