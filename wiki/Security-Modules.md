With optional security modules life should be made easier for developers by disabling security features.

# security-jwt

The microprofile JWT security is activated by default. It allows us to use the Keycloak or other identity providers for
authentication or authorization. In this case, we acquire all necessary data from bearer tokens sent in the
authorization header.

## Compile

```
mvn clean package -Psecurity-jwt
```

## Testing

To test this locally, you can install a Keycloak instance locally in a container and configure it.

TODO: Documentation of local testing

# security-dummy

The configuration of an identity provider for the local environment is an overhead, if a developer works on features
that don't involve security directly. For such scenarios, Hogarama can be built and started with the dummy security
module.

## Compile

```
mvn clean package -Psecurity-dummy
```

## Test

After deployment, all REST-Services accept the dummy security header in the form
`"Authorization: Dummy <BASE64 encoded dummy user json object>"`. The user object has the following format:

```
{
  "name": "...",
  "email": "...",
  "givenName": "...",
  "familyName": "..."
}
```

This dummy security header allows also simulate different users, because the backend uses email as a unique user
identifier.

In order to perform a quick test, whether the dummy security is configured correctly, you can run the script
`hogajama-rs/src/test/resources/testRest.sh`. The script makes several calls with a dummy security header. If you do not
see any `401` or `403` response codes in the script outputs (expected are `200` or `500` with exception message), then
everything is configured correctly.

You can also configure a dummy security user in the angular
frontend ([http://localhost:8080/unitmanagement](http://localhost:8080/unitmanagement)) under the "Login" section.
