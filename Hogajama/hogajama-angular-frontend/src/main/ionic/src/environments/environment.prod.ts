const keycloakConfig = {
  url: 'https://keycloak-gepardec.apps.p.aws.ocp.gepardec.com/auth/',
  realm: 'hogarama',
  clientId: 'hogarama',
  credentials : {secret : '12360720-0484-40da-9459-34ce53121677'}
};

export const environment = {
  production: true,
  keycloak: keycloakConfig,
  keycloakLibrarySubUrl: 'js/keycloak.js',
  keycloakTokenMinValidity: 30,
  backendUrl: '/hogajama-rs'
};
