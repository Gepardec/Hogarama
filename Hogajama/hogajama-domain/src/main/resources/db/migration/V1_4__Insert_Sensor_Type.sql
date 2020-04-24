SET search_path = "hogajama";

DELETE FROM sensor_type;
INSERT INTO sensor_type ("id", "name")
VALUES
    (1, 'IDENTITY'),
    (2, 'LINEAR100'),
    (3, 'LINEAR1024'),
    (4, 'INVERSE_LINEAR1024'),
    (5, 'Chinese Water Sensor'),
    (6, 'sparkfun');
