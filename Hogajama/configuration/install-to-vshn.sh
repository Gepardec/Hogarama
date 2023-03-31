#!/bin/sh
# login to Dockerhub and Cluster
# Create a JBSS Instance hogavshn with environment vshn.env
# You need a cleanly checked out hogajama-angular-frontend. Dont build it locally before running this!

hogavshn configure build_artefacts_locally.sh
hogavshn configure build_container_locally.sh

docker tag hogajama-run gepardec/hogajama-run:latest
docker push gepardec/hogajama-run:latest

oc import-image hogajama-run --from=gepardec/hogajama-run --all --confirm
