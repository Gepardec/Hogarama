SET search_path = "hogajama";

create sequence seq_actor_id
    increment by 50;

alter sequence seq_actor_id owner to hogajama;

create table actor
(
    id        bigint       not null
        constraint pk_actor
            primary key,
    device_id varchar(255) not null
        constraint pui_actor_device_id
            unique,
    name      varchar(255),
    unit_id   bigint       not null
        constraint fk_actor_unit
            references unit
);

alter table actor
    owner to hogajama;

