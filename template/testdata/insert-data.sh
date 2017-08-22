#!/bin/bash

/opt/rh/rh-mongodb32/root/usr/bin/mongo hogajamadb --eval "load('/tmp/testdata/insert.js')"
/opt/rh/rh-mongodb32/root/usr/bin/mongoimport -u "hogajama" -p "hogajama@mongodb" -d hogajamadb -c habarama --type json /tmp/testdata/hogarama-testdata.json
