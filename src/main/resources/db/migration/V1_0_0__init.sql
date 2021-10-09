create table place
(
    id          bigserial primary key not null,
    name        text,
    address     text                  not null,
    description text
);

create table event
(
    id                         bigserial primary key not null,
    name                       text                  not null,
    time                       timestamp             not null,
    duration                   integer,
    place_id                   bigserial             not null,
    max_number_of_participants integer,
    number_of_participants     integer               not null,
    description                text,
    foreign key (place_id) references place
);