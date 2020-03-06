#!/bin/bash

helm repo add postgresql https://charts.bitnami.com/bitnami
helm install postgresql postgresql/postgresql -f values.yaml || helm upgrade postgresql postgresql/postgresql -f values.yaml