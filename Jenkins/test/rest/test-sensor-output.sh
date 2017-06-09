#!/usr/bin/bash

source test-function.sh

PATH_OK="https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs/rest/sensor/allData?maxNumber=2&sensor=Pflanze"
PATH_FAILURE="https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs/rest/sensor/allData?maxNumber=2&sensor=Meowmeow"

test $PATH_FAILURE \\[\\] 
test $PATH_OK \\[\\{\"id\":.*\\},\\{\"id\":.*\\}\\] 
