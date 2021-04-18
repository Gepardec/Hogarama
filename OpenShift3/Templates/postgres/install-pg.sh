#!/bin/bash

helm repo add bitnami https://charts.bitnami.com/bitnami
helm upgrade -i postgresql bitnami/postgresql --version=9.3.2 -f values.yaml