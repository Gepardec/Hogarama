#!/bin/bash

helm repo add postgresql https://charts.bitnami.com/bitnami
helm upgrade -i postgresql postgresql/postgresql -f values.yaml