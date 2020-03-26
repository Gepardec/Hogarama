# A Helm Chart for installing Hogarama on your kubernetes/openshift cluster

## Prerequisites for helm chart: 
- Install Keycloak via provided bash scripts and follow its readme for additional steps
- Install Hogarama_Commons via provided bash scripts

## Templating values.yml.j2
- run `bootstrap/wrapper/hogarama_template.sh --resource helm`

# Hogarama_Infrastructure
Infrastructure Code for Deployments on OpenShift

This is an active work in progress and at the moment not in any production environment yet. 
The goal of this repository is to have a place for all infrastructure code that for our gepardec/Hogarama repository

The Required secrets.yml File for Gepardec Production Environment can be found in our Google Drive. 
* GEPARDEC-DRIVE/PROJEKTE/INTERN/LEARNING-FRIDAY/SONSTIGES/HOGARAMA-Resources/