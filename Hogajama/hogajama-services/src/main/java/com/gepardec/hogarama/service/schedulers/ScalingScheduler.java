package com.gepardec.hogarama.service.schedulers;

import java.lang.management.ManagementFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;

import org.slf4j.Logger;

import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.model.IDeploymentConfig;

@Startup
@Singleton
public class ScalingScheduler {

	private Boolean scaledUp = false;

	private MBeanServer mBeanServer = null;

	@Inject
	private Logger log;

	@Schedule(hour = "*", minute = "*", second = "1", info = "Every minute")
	public void checkSessions() {
		log.debug("Current Active Sessions: {}", getActiveSessions());

		if (shouldStartNewPod()) {
			IClient client = getOpenshiftClient();
			scaleUp(client);
			scaledUp = true;
		}

	}

	private boolean shouldStartNewPod() {
		return !scaledUp && getActiveSessions() > 3;
	}
	
	@Schedule(hour = "*", minute = "*/10", info = "Every Minute")
	public void resetScaleUp() {
		log.info("Reset scaledUp.");
		scaledUp = false;
	}

	private int getActiveSessions() {

		if (mBeanServer == null) {
			mBeanServer = ManagementFactory.getPlatformMBeanServer();
		}

		int activeSessions = 0;

		try {
			for (ObjectInstance objectInstance : mBeanServer
					.queryMBeans(new ObjectName("jboss.as:deployment=*,subsystem=undertow"), null)) {
				activeSessions += (Integer) mBeanServer.getAttribute(objectInstance.getObjectName(), "activeSessions");
			}
		} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException | MBeanException
				| ReflectionException e) {
			throw new RuntimeException(e);
		}

		return activeSessions;
	}

	private IClient getOpenshiftClient() {

		IClient client = new ClientBuilder("https://manage.cloud.itandtel.at")
				// .withUserName("openshiftdev")
				// .withPassword("wouldntUlik3T0kn0w")
				.build();

		// Retreive auth token from ENV
		String authToken = System.getenv("OPENSHIFT_AUTH_TOKEN");
		if (authToken == null || authToken.isEmpty()) {
			throw new RuntimeException("Cannot auth to openshift: OPENSHIFT_AUTH_TOKEN not found");
		}
		client.getAuthorizationContext().setToken(authToken);

		return client;
	}

	private void scaleUp(IClient client) {

		String namespace;
		String dcConfig;

		namespace = System.getenv("CURRENT_NAMESPACE");
		if (namespace == null || namespace.isEmpty()) {
			throw new RuntimeException("Cannot retrieve current namespace: CURRENT_NAMESPACE not found");
		}

		// TODO DeploymentConfig should be retrieved dynamically
		dcConfig = "hogajama";

		IDeploymentConfig depconfig = client.get(ResourceKind.DEPLOYMENT_CONFIG, dcConfig,
				namespace);

		int replicas = depconfig.getDesiredReplicaCount();

		if (replicas >= 3) {
			return;
		}

		depconfig.setDesiredReplicaCount(replicas + 1);

		client.update(depconfig);

		log.info("Scale up to replica count of: {}", replicas + 1);
	}
}
