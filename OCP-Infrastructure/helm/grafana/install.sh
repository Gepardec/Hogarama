#!/bin/sh

helm template helm-grafana . -f ../secrets/values.yaml
