package com.gepardec.hogarama.security.jwt;

import org.eclipse.microprofile.auth.LoginConfig;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Activating JAX-RS annotation based.
 */
@ApplicationPath("/rest")
@LoginConfig(authMethod="MP-JWT", realmName="MP JWT Realm")
public class JaxRsActivator extends Application {

}
