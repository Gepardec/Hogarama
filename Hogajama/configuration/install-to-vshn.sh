#!/bin/sh
# login to Dockerhub and Cluster
# Create a JBSS Instance hogavshn with environment vshn.env
# You need a cleanly checked out hogajama-angular-frontend. Dont build it locally before running this!

hogavshn configure build_artefacts_locally.sh || exit -1
hogavshn configure build_container_locally.sh || exit -1

docker tag hogajama-run gepardec/hogajama-run:latest || exit -1
docker push gepardec/hogajama-run:latest || exit -1

oc import-image hogajama-run --from=gepardec/hogajama-run --all --confirm || exit -1
