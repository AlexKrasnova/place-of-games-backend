create table place
(
    id      bigserial primary key not null,
    name    text,
    address text                  not null,
    version integer
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
    version                    integer,
    foreign key (place_id) references place
);

insert into place (name, address)
values ('Стадион Металлист', 'г. Харьков, ул. Плехановская, д. 65'),
       ('Котокафе', 'г. Харьков, ул. Полтавский шлях, д. 66'),
       ('Стадион Лужники', 'г. Москва, прсп. Комсомольский, д.13'),
       ('Шоколадница', 'г. Москва, ул. Тверская, д. 14'),
       ('Шоколадница', 'г. Москва, ул. Большая Никитская, д.2'),
       ('Боулинг-клуб', 'г. Москва, прсп. Вернадского, д. 133');

insert into event (name, time, duration, place_id, max_number_of_participants, number_of_participants)
values ('Футбол', '2021-11-01 11:00:00', 120, 1, 22, 15),
       ('Футбол', '2021-11-08 11:00:00', 120, 1, 22, 6),
       ('Настольный теннис', '2021-10-24 13:00:00', 60, 4, 2, 1),
       ('Настольный теннис', '2021-10-15 19:00:00', 60, 4, 4, 1),
       ('Футбол', '2021-12-02 10:00:00', 120, 3, 22, 13),
       ('Футбол', '2021-11-03 19:00:00', 120, 3, 22, 15),
       ('Боулинг', '2021-10-13 19:00:00', 90, 6, 4, 2),
       ('Турнир по шахматам', '2021-11-05 18:30:00', 120, 2, 8, 5),
       ('Турнир по шахматам', '2021-11-09 19:00:00', 120, 2, 8, 3),
       ('Баскетбол', '2021-10-18 18:00:00', 120, 3, 16, 0);