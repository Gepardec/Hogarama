#!/bin/sh

helm template helm-amq . -f ../secrets/values.yaml
