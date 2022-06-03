#!/bin/sh

helm template helm-hogajama . -f ../secrets/values.yaml
