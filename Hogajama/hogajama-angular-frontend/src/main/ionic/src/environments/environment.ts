// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// Add here your keycloak setup infos
const keycloakConfig = {
  url: 'https://keycloak-hogarama.apps.play.gepaplexx.com/auth/',
  realm: 'hogarama',
  clientId: 'hogarama',
  credentials : {secret : '12360720-0484-40da-9459-34ce53121677'}
};

export const environment = {
  production: false,
  keycloak: keycloakConfig,
  keycloakLibrarySubUrl: 'js/keycloak.js',
  keycloakTokenMinValidity: 30,
  backendUrl: '/hogajama-rs'
};
