# Postgres on OpenShift via HelmChart

## Prerequisits

- Install `oc` and `helm`
- Configure PATH, so that `oc` and `helm` are visible

## Installation
- Perform the login into a project via `oc`:
    - `oc login --token=<TOKEN> --server=<SERVER>`
    - `oc project <PROJECT NAME>`
- Install postgres
    - Switch to the directory with this README.md
    - Define database name, user and password in `values.yaml`
    - Linux : `./install-pg.sh`
    - Windows: `./intall-pg.cmd`