create table role_table
(
    name varchar(20) primary key not null
);

create table user_table
(
    id       bigserial primary key,
    login    varchar(50) unique not null,
    password varchar(500),
    name     varchar(50)
);

create unique index login_index on user_table (login);

create table user_role_link
(
    user_id   bigserial   not null,
    role_name varchar(20) not null,
    primary key (user_id, role_name),
    foreign key (user_id) references user_table,
    foreign key (role_name) references role_table
);

insert into role_table (name)
values ('ROLE_USER');