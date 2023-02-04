curl --header "Content-Type: application/json" http://localhost:8080/hogajama-rs/rest/messaging/sensorData -d \
'{
    "sensorName" : "Pflanze",
    "type" : "sparkfun",
    "value" : 700,
    "location" : "Wien",
    "version" : 0
}'
