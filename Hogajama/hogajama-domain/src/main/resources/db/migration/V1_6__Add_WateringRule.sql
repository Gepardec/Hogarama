SET search_path = "hogajama";

create sequence seq_rule_id
    increment by 50;

alter sequence seq_rule_id owner to hogajama;

create table low_water_watering_rule
(
    id        bigint       not null
        constraint pk_watering_rule
            primary key,
    name      varchar(255) not null,
    description varchar(255),
    sensor_id   bigint       not null
        constraint fk_sensor
            references sensor,
    actor_id   bigint       not null
        constraint fk_actor
            references actor,
    water_duration int       not null,
    low_water      float     not null,
    unit_id   bigint       not null
        constraint fk_actor_unit
            references unit
);

alter table low_water_watering_rule
    owner to hogajama;

