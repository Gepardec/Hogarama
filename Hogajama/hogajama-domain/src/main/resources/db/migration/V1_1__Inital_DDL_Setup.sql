SET search_path = "hogajama";

create sequence seq_owner_id
    increment by 50;

alter sequence seq_owner_id owner to hogajama;

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
    id          bigint       not null
        constraint pk_owner
            primary key,
    sso_user_id varchar(255) not null
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

