# Single Sign On

For authentication and authorisation on restricted resources we use RedHat SSO based on Keycloak.
It must be installed as a separate service with PostgreSQL support on openshift.

In order to install RedHat SSO on a local OpenShift instace it is sufficient to roll out local Openshift via templates:
**${HOGARAMA_HOME_DIR}/Templates/local_openshift/startAll.sh**

Also you can find the templates for SSO configuration under **${HOGARAMA_HOME_DIR}/Templates/sso**

## SSO-Configuration

After installation you can login into admin area using user and password from template sso.yaml (find them in section *
*environment variables**) and perform following actions:

1. Create realm with name "hogarama"
2. Click "import" and import **${HOGARAMA_HOME_DIR}/Templates/sso/config.json** (select 'Skip' in field _If resource
   exists_)
3. Change password of user 'admin' under _users->view all users->admin->edit->creadentials_. Enter new password and
   assign **temporary=false**

## Hohgajama(EAP)-Configuration

After the rollout Hogajama is already preconfigured. Ensure that the value of the environment variable *
*KEYCLOAK_AUTH_SERVER_URL** of the 'hogajama' deployment is equal to <hogajama.route.hostname>/auth/. E.g.: default on
local environment will be https://secure-sso-hogarama.10.0.75.2.nip.io/auth/

EAP 7.1 for openshift has already preinstalled keyclock-agent. We only need to configure standalone.xml and application.

### Standalone.xml

1. Enable keyclock extention

```
<!-- ##KEYCLOAK_EXTENSION## -->
<extension module="org.keycloak.keycloak-adapter-subsystem"/>
```

2. Add sequrity domain

```
<subsystem xmlns="urn:jboss:domain:security:2.0">
   ...
      <security-domain name="keycloak">
         <authentication>
            <login-module code="org.keycloak.adapters.jboss.KeycloakLoginModule" flag="required"/>
         </authentication>
      </security-domain>
      <!-- ##ADDITIONAL_SECURITY_DOMAINS## -->
   </security-domains>
</subsystem>
```

3. Enable and configure keycloak subsystem

```
<!-- ## KEYCLOAK SUBSYSTEM -->
<subsystem xmlns="urn:jboss:domain:keycloak:1.1"/>
```

### WebApp

Following resources must be configured:

* src/main/webapp/WEB-INF/web.xml
* src/main/webapp/WEB-INF/jboss-web.xml
* src/main/webapp/WEB-INF/keycloak.json

Example web.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">

	<welcome-file-list>
		<welcome-file>/index.html</welcome-file>
	</welcome-file-list>
	
	<security-constraint>
		<web-resource-collection>
			<web-resource-name>Restricted</web-resource-name>
			<url-pattern>/admin.html</url-pattern>
		</web-resource-collection>
		<auth-constraint>
			<role-name>admins</role-name>
		</auth-constraint>
	</security-constraint>
	
	<login-config>
		<auth-method>KEYCLOAK</auth-method>
	</login-config>
	
	<security-role>
		<role-name>admins</role-name>
	</security-role>

</web-app>
```

Example jboss-web.xml

```
<jboss-web xmlns="http://www.jboss.com/xml/ns/javaee"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.jboss.com/xml/ns/javaee http://www.jboss.org/j2ee/schema/jboss-web_6_0.xsd"
	version="6.0">
	<context-root>/</context-root>
	<security-domain>keycloak</security-domain>
</jboss-web>
```

Example keycloak.json

```
{
	"realm": "hogarama",
	"auth-server-url": "${env.KEYCLOAK_AUTH_SERVER_URL}",
	"ssl-required": "external",
	"resource": "hogarama",
	"credentials": {"secret":"12360720-0484-40da-9459-34ce53121677"},
	"disable-trust-manager":true,
	"allow-any-hostname": true
}
```

keycloak.json can be generated in SSO WebConsole: Clients->Hogarama-Installations->Keycloak OIDC JSON

## Test

1. Open < hogajama-url >/admin.html. You will be redirected to Redhat SSO
2. Enter username 'admin' and your configured password
3. You will be redirected back on the restricted page and should be able to see restricted content.