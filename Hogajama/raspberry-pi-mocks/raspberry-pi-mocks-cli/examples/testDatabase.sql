insert into owner ("id", "sso_user_id") values(1, 'aa5f33ff-3c31-4250-95a1-529b76d3be1e');
insert into unit ("id", "description", "is_default", "name", "owner_id") values(2, 'test unit', false, 'testland', 1);
insert into sensor ("id", "device_id", "name", "sensor_type_id", "unit_id") values(1, 'Pflanze', 'Test Pflanze', 2, 2);
insert into sensor ("id", "device_id", "name", "sensor_type_id", "unit_id") values(2, 'Pflanze 2', 'Test Pflanze', 2, 2);
insert into actor ("id", "device_id", "name", "unit_id", "queue_name") values(1, 'Pumpe', 'Pumpe für Pflanze', 2, 'queue_name fehlt in ...');
insert into wateringrule ("id", "sensor_id", "actor_id", "unit_id", "name", "description", "low_water", "water_duration") values(202, 1, 1, 2, 'Pflanze_Pumpe', 'Gieße Testland', 0.5, 20);