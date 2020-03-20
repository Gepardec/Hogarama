# A Helm Chart for installing Hogarama on your kubernetes/openshift cluster

## Prerequisites for helm chart: 
- Install Keycloak via provided bash scripts and follow its readme for additional steps
- Install Hogarama_Commons via provided bash scripts

## Templating values.yml.j2
- run `bootstrap/wrapper/hogarama_template.sh --resource helm`