ALTER TABLE blubb
    ADD COLUMN sensor_name text,
    ADD COLUMN sensor_location text,
    ADD COLUMN actor_name text,
    ADD COLUMN actor_location text;

INSERT INTO blubb (sensor_name, sensor_location, actor_name, actor_location ) values('GruenerGepard', 'Wien', 'Gruene Pumpe', 'Wien');