#!/bin/bash

#find src/main/webapp/ | grep -v "WEB-INF" | grep -v "^src/main/webapp/$" | xargs rm -rf
rm -rf src/main/webapp/*

cd angular2/
ng build --output-path=../src/main/webapp/ --target=production --progress=true
cp -r WEB-INF/ ../src/main/webapp/
cd -

echo "Build angular2 to /src/main/webapp/"
