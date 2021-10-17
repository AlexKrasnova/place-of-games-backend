create table event_participant
(
    user_login varchar(50) not null,
    event_id  bigserial not null,
    primary key (user_login, event_id),
    foreign key (user_login) references user_table,
    foreign key (event_id) references event on delete cascade on update cascade
);

alter table event
drop column number_of_participants,
add column version bigserial;

update event set version = 0;