insert into "user" ("id", "key") values(1, 'aa5f33ff-3c31-4250-95a1-529b76d3be1e');
insert into unit ("id", "description", "is_default", "name", "user_id") values(2, 'test unit', false, 'testland', 1);
insert into sensor ("id", "device_id", "name", "sensor_type_id", "unit_id") values(1, 'Pflanze', 'Test Pflanze', 2, 2);
insert into sensor ("id", "device_id", "name", "sensor_type_id", "unit_id") values(2, 'Pflanze 2', 'Test Pflanze', 2, 2);
insert into actor ("id", "device_id", "name", "unit_id", "queue_name") values(1, 'Pumpe', 'Pumpe für Pflanze', 2, 'queue_name fehlt in ...');
insert into low_water_watering_rule ("id", "sensor_id", "actor_id", "unit_id", "name", "description", "low_water", "water_duration") values(202, 1, 1, 2, 'Pflanze_Pumpe', 'Gieße Testland', 0.5, 20);

delete from low_water_watering_rule where unit_id = 20;
delete from actor where unit_id = 20;
delete from sensor where unit_id = 20;
delete from unit where id = 20;
delete from "user" where id = 10;
delete from "user" where "user".key = 'erhard';
insert into "user" ("id", "key") values(10, 'erhard');
insert into unit ("id", "description", "is_default", "name", "user_id") values(20, 'unit20', false, 'testland', 10);
insert into sensor ("id", "device_id", "name", "sensor_type_id", "unit_id") values(10, 'Pflanze10', 'Test Pflanze', 2, 20);
insert into actor ("id", "device_id", "name", "unit_id", "queue_name") values(10, 'Pumpe10', 'Pumpe für Pflanze', 20, 'queue_name fehlt in ...');
insert into low_water_watering_rule ("id", "sensor_id", "actor_id", "unit_id", "name", "description", "low_water", "water_duration") values(30, 10, 10, 20, 'Pflanze_Pumpe30', 'Gieße Testland', 0.5, 20);
