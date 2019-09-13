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
	id bigint not null
		constraint owner_pkey
			primary key,
	sso_user_id bigint
);

alter table owner owner to hogajama;

create table sensor_type
(
	id bigint not null
		constraint sensor_type_pkey
			primary key,
	name varchar(255)
);

alter table sensor_type owner to hogajama;

create table unit
(
	id bigint not null
		constraint unit_pkey
			primary key,
	description varchar(255),
	name varchar(255),
	owner_id bigint
		constraint fkqw70o6thoj3qu6stp022k6jph
			references owner
);

alter table unit owner to hogajama;

create table actor
(
	id bigint not null
		constraint actor_pkey
			primary key,
	device_id varchar(255)
		constraint uk_fovpxrjovo1lp1q3kjuirw44m
			unique,
	name varchar(255),
	unit_id bigint
		constraint fkjwsymcfnaw5g1plswhr6c4psx
			references unit
);

alter table actor owner to hogajama;

create table rule
(
	id bigint not null
		constraint rule_pkey
			primary key,
	name varchar(255),
	unit_id bigint
		constraint fkn6wflw8wpgl1flx2gkcbwpto2
			references unit
);

alter table rule owner to hogajama;

create table sensor
(
	id bigint not null
		constraint sensor_pkey
			primary key,
	device_id varchar(255)
		constraint uk_ppmi5j5lawo8147qlw48a2pmd
			unique,
	name varchar(255),
	sensor_type_id bigint
		constraint fk9jx27gc38xq7cnulbhyljt422
			references sensor_type,
	unit_id bigint
		constraint fkpsv310c99n50uclpg6n81jaeh
			references unit
);

alter table sensor owner to hogajama;

create table single_actor_rule
(
	id bigint not null
		constraint single_actor_rule_pkey
			primary key
		constraint fki1946xtg3gjv8076acxojlh58
			references rule,
    interval_between_waterings_in_hours integer,
    watering_duration_in_seconds integer,
	actor_id bigint
		constraint fkku5jy3px2tap1rtkay5p7naid
			references actor
);

alter table single_actor_rule owner to hogajama;

create table single_sensor_actor_rule
(
	id bigint not null
		constraint single_sensor_actor_rule_pkey
			primary key
		constraint fknnmamjyqd4ni01gbt6jpnff43
			references rule,
    moisture_threshold numeric(19,2),
    watering_duration_in_seconds integer,
	actor_id bigint
		constraint fkdbg3lw6ajqkbi860tot0xas5s
			references actor,
	sensor_id bigint
		constraint fk7p69t0h9hwe21np853ab4fg3w
			references sensor
);

alter table single_sensor_actor_rule owner to hogajama;

create table single_sensor_rule
(
	id bigint not null
		constraint single_sensor_rule_pkey
			primary key
		constraint fkfouwfvntknav70rj0l2kkbbnq
			references rule,
    moisture_threshold numeric(19,2),
    sensor_id bigint
		constraint fkin69mq6k7hof29vqwwrvgmlnv
			references sensor
);

alter table single_sensor_rule owner to hogajama;
