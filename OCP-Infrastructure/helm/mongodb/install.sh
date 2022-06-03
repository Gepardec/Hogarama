#!/bin/sh

helm template helm-mongodb . -f ../secrets/values.yaml
