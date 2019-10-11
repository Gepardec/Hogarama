create sequence seq_actor_id
    increment by 50;

alter sequence seq_actor_id owner to hogajama;

create sequence seq_owner_id
    increment by 50;

alter sequence seq_owner_id owner to hogajama;

create sequence seq_rule_id
    increment by 50;

alter sequence seq_rule_id owner to hogajama;

create sequence seq_sensor_id
    increment by 50;

alter sequence seq_sensor_id owner to hogajama;

create sequence seq_sensor_type_id
    increment by 50;

alter sequence seq_sensor_type_id owner to hogajama;

create sequence seq_unit_id
    increment by 50;

alter sequence seq_unit_id owner to hogajama;

create table owner
(
    id          bigint not null
        constraint pk_owner
            primary key,
    sso_user_id bigint not null
);

alter table owner
    owner to hogajama;

create table sensor_type
(
    id   bigint       not null
        constraint pk_sensor_type
            primary key,
    name varchar(255) not null
);

alter table sensor_type
    owner to hogajama;

create table unit
(
    id          bigint                not null
        constraint pk_unit
            primary key,
    description varchar(255),
    is_default  boolean default false not null,
    name        varchar(255),
    owner_id    bigint                not null
        constraint fk_unit_owner
            references owner
);

alter table unit
    owner to hogajama;

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

create table rule
(
    id      bigint       not null
        constraint pk_rule
            primary key,
    name    varchar(255) not null,
    unit_id bigint       not null
        constraint fk_rule_unit
            references unit
);

alter table rule
    owner to hogajama;

create table sensor
(
    id             bigint       not null
        constraint pk_sensor
            primary key,
    device_id      varchar(255) not null
        constraint pui_sensor_device_id
            unique,
    name           varchar(255),
    sensor_type_id bigint       not null
        constraint fk_sensor_sensor_type
            references sensor_type,
    unit_id        bigint       not null
        constraint fk_sensor_unit
            references unit
);

alter table sensor
    owner to hogajama;

create table single_actor_rule
(
    id                                  bigint  not null
        constraint pk_single_actor_rule
            primary key
        constraint fk_single_actor_rule_rule
            references rule,
    interval_between_waterings_in_hours integer not null,
    watering_duration_in_seconds        integer not null,
    actor_id                            bigint  not null
        constraint fk_single_actor_rule_actor
            references actor
);

alter table single_actor_rule
    owner to hogajama;

create table single_sensor_actor_rule
(
    id                          bigint         not null
        constraint pk_single_sensor_actor_rule
            primary key
        constraint fk_single_sensor_actor_rule_rule
            references rule,
    moisture_threshold          numeric(19, 2) not null,
    watering_duration_in_secods integer        not null,
    actor_id                    bigint         not null
        constraint fk_single_sensor_actor_rule_actor
            references actor,
    sensor_id                   bigint         not null
        constraint fk_single_sensor_actor_rule_sensor
            references sensor
);

alter table single_sensor_actor_rule
    owner to hogajama;

create table single_sensor_rule
(
    id                 bigint         not null
        constraint pk_single_sensor_rule
            primary key
        constraint fk_single_sensor_rule_rule
            references rule,
    moisture_threshold numeric(19, 2) not null,
    sensor_id          bigint         not null
        constraint fk_single_sensor_rule_sensor
            references sensor
);

alter table single_sensor_rule
    owner to hogajama;

