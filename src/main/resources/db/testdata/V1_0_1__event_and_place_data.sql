insert into place (name, address, description)
values ('Стадион Металлист', 'г. Харьков, ул. Плехановская, д. 65', 'Самый крутой стадион во всем Харькове'),
       ('Котокафе', 'г. Харьков, ул. Полтавский шлях, д. 66',
        'Котики, кофе и уютная компания, что еще нужно для счастья?'),
       ('Стадион Лужники', 'г. Москва, прсп. Комсомольский, д.13', 'Самый крутой стадион во всей Москве'),
       ('Шоколадница', 'г. Москва, ул. Тверская, д. 14', 'Уютное кафе в двух минутах от метро Охотный ряд'),
       ('Шоколадница', 'г. Москва, ул. Большая Никитская, д.2', 'Тихое, спокойное место для встреч с друзьями'),
       ('Боулинг-клуб', 'г. Москва, прсп. Вернадского, д. 133',
        'Замечательный боулинг-клуб для того, чтобы отлично провести время в хорошей компании')
ON CONFLICT DO NOTHING;

insert into event (name, time, duration, place_id, max_number_of_participants, description, category, version)
values ('Футбол', '2021-11-01 11:00:00', 120, 1, 22, 'Любительская игра', 'FOOTBALL', 0),
       ('Футбол', '2021-11-08 11:00:00', 120, 1, 22, 'Любительская игра', 'FOOTBALL', 0),
       ('Настольный теннис', '2021-10-24 13:00:00', 60, 4, 2, 'Любительская игра', 'TENNIS', 0),
       ('Настольный теннис', '2021-10-15 19:00:00', 60, 4, 4, 'Любительская игра', 'TENNIS', 0),
       ('Футбол', '2021-12-02 10:00:00', 120, 3, 22, 'Любительская игра', 'FOOTBALL', 0),
       ('Футбол', '2021-11-03 19:00:00', 120, 3, 22, 'Любительская игра', 'FOOTBALL', 0),
       ('Боулинг', '2021-10-13 19:00:00', 90, 6, 4, 'Крутая тусовка', 'BOWLING', 0),
       ('Турнир по шахматам', '2021-11-05 18:30:00', 120, 2, 8, 'Профессиональный уровень', 'CHESS', 0),
       ('Турнир по шахматам', '2021-11-09 19:00:00', 120, 2, 8, 'Профессиональный уровень', 'CHESS', 0),
       ('Баскетбол', '2021-10-18 18:00:00', 120, 3, 16, 'Любительская игра', 'BASKETBALL', 0),
       ('Футбол', '2021-11-01 15:00:00', 90, 1, 22, 'Любительская игра', 'FOOTBALL', 0),
       ('Футбол', '2021-11-01 19:00:00', 120, 1, 22, 'Любительская игра', 'FOOTBALL', 0)
ON CONFLICT DO NOTHING;