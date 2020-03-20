# Offene Punkte

- Freitag 07.02.2020
    - Kontrolle AMQ fertig (über MQTT Spy ausprobieren, ob Nachrichten ankommen)
    - Secret File erstellen, sourcen und Secrets über Variable in yaml Files bringen
    - Keycloak fertig (default groups und role mappings)
        1. realm = hogarama nennen, ansonsten spießt es sich mit der keycloak json im war file (Rückwärtskompatibilität mit itandtel)
        2. group anlegen, default group für neue user 
    - Templating von Konfigurationen
        - e.g. Dashboard & Datasource Grafana umstellen auf AWS Instanzen
        - Operator Group !!!! (target namespace/namespace der OG)
        - eap-crd: registry namespace  
    - IMPORTANTE: revoke gepardec client secret & key. keycloak realm parametrisiert mit diesen anlegen. nicht in github speichern
    - PVCs
    - Script deletion of all namespaces containing an operator: 
        1. delete custom resources provided by operator
        2. delete operator suscription
        3. delete all secrets
        4. delete all --all -n ${namespace}
        5. delete project ${namespace}
        
        6. in case something goes wrong rejoice and hail noam manos on stackoverflow and upvote https://stackoverflow.com/questions/58638297/project-deletion-struck-in-terminating

- Force Flag durchpipen zu allen Wrappern von bootstrap-wrapper.sh
- Nexus?
- Jenkins?
- Configure GitHub webhook for hogajama-binary build
- AMQ Online über Operator
    - resolve differences in AMQ configuration between itandtel/A- AMQ MQTT-SSL Acceptor - our current SSL Version is not supported due to a found vulnerability