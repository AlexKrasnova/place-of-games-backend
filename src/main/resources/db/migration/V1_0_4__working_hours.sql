create table working_hours
(
    id          bigserial primary key not null,
    place_id    bigint                not null,
    day_of_week varchar(30),
    date        date,
    start_time  time                  not null,
    end_time    time                  not null,
    foreign key (place_id) references place on delete cascade on update cascade
)