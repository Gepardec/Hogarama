#!/usr/bin/bash

source test-function.sh

URL_SENSOR="https://hogajama-57-hogarama.cloud.itandtel.at/hogajama-rs/rest/sensor/"

test $URL_SENSOR \\[.*\"Pflanze\".*\\]
