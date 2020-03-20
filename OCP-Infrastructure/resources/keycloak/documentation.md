# KEYCLOAK DOCUMENTATION

Currently there is no (known) way to setup groups and roles with the keycloak operator. 
Unfortunately, this leads to a few manual steps required to properly setup keycloak. 

1. after running the keycloak scripts log in to the keycloak administration console
    * default user: admin
    * password: can be found under secrets in the namespace in which keycloak got installed with the name credentials-${keycloak-realm-name}
2. import "realm_groups_users.json" (button import)
3. click on groups -> default groups -> /admins -> add
4. done