create table event_participant
(
    user_id bigserial not null,
    event_id  bigserial not null,
    primary key (user_id, event_id),
    foreign key (user_id) references user_table,
    foreign key (event_id) references event on delete cascade on update cascade
);