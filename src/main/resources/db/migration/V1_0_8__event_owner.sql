alter table event
    add column owner_id bigserial;

alter table event
    add foreign key (owner_id) references user_table;