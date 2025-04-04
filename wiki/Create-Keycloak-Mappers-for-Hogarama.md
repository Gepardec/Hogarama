Hogarama needs the following 4 informations from keycloak for identifying the user and further processing.

* name
* email
* given name
* family name

Therefore mappers in the Keycloak client need to be defined. For now this process is not automated and this step has to
be done manually at the moment.

## Where is the mappers option in Keycloak for Hogarama?

1. Login to the Keycloak administration console (https://keycloak-hogarama.apps.steppe.gepaplexx.com/auth/admin/)

2. Go to `Clients` --> hogarama-frontend

![1](https://user-images.githubusercontent.com/38063612/193138086-cc436628-4af0-4255-8f8f-8fcc2285342a.png)

3. Go to the `Mappers`-Tab

## Create the Mappers for Hogarama

### Add Builtin mappers

Three of the informations mentioned at the beginning of this document can be mapped by builtin mappers.

1. In the `Mappers` Tab click on `Add Builtin`
2. Choose `email`, `family name` and `given name`
3. Confirm with `Add selected`

### Create custom mapper

The `name` property is not builtin and need to be configured manually.

1. In the `Mappers` Tab click on `Create`
2. Create the Mapper with the following settings:
    1. Name: `name`
    2. Mapper Type: `User Property`
    3. Property: `username`
    4. Token Claim Name: `name`
3. Click on `Save`

The following screenshot shows the properties described in step 2:

![image](https://user-images.githubusercontent.com/38063612/193140360-29702e4d-8eac-48fd-aecf-d9da12d49a8f.png)

## Analyze the Bearer Token

Hogarama shows your Bearer Token after the login. You can decode this Bearer Token on https://jwt.io/ to see the
attributes in readable form. Look out for the attributes `name`, `given_name`, `family_name` and `email`.