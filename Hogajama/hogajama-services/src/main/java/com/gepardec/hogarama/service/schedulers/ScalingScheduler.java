package com.gepardec.hogarama.service.schedulers;

import java.io.StringReader;
import java.lang.management.ManagementFactory;
import java.util.Collection;

import com.openshift.restclient.model.IEnvironmentVariable;

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
import com.openshift.restclient.model.IReplicationController;
import com.openshift.internal.restclient.model.ReplicationController;
import com.openshift.restclient.model.kubeclient.ICluster;
import com.openshift.restclient.model.kubeclient.IContext;
import com.openshift.restclient.model.kubeclient.IKubeClientConfig;
import com.openshift.restclient.model.kubeclient.IUser;
import com.openshift.restclient.model.kubeclient.KubeClientConfigSerializer;
import com.openshift.restclient.utils.*;
import com.openshift.internal.restclient.model.ModelNodeBuilder;
import com.openshift.internal.restclient.model.ReplicationController;
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.internal.restclient.model.volume.EmptyDirVolumeSource;
import com.openshift.internal.restclient.model.volume.SecretVolumeSource;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;
import com.openshift.restclient.images.DockerImageURI;
import com.openshift.restclient.model.IConfigMapKeySelector;
import com.openshift.restclient.model.IContainer;
import com.openshift.restclient.model.IEnvironmentVariable;
import com.openshift.restclient.model.IEnvironmentVariable.IEnvVarSource;
import com.openshift.restclient.model.IObjectFieldSelector;
import com.openshift.restclient.model.IPort;
import com.openshift.restclient.model.IReplicationController;
import com.openshift.restclient.model.ISecretKeySelector;
import com.openshift.restclient.model.volume.IVolume;
import com.openshift.restclient.model.volume.IVolumeMount;
import com.openshift.restclient.model.volume.IVolumeSource;
import com.openshift.restclient.utils.Samples;


import org.jboss.dmr.ModelNode;

@Startup
@Singleton
public class ScalingScheduler {

	private Boolean scaledUp = false;

	private MBeanServer mBeanServer = null;

	@Inject
	private Logger log;
	
	@Schedule(hour = "*", minute = "*", second = "*", info = "Every second")
	public void checkSessions() {
		log.info("Current Acitve Sessions: {}", getActiveSessions());
		
		// Tell openshift to start another pod 
		if(! scaledUp) {
			if(getActiveSessions() > 7){
				IClient client = getOpenshiftClient();
				scaleUp(client);
				scaledUp = true;
			}
		}
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

	private IClient getOpenshiftClient(){

		IClient client = new ClientBuilder("https://manage.cloud.itandtel.at")
			// .withUserName("openshiftdev")
			// .withPassword("wouldntUlik3T0kn0w")
		.build();

		// Retreive auth token from ENV
		String authToken = System.getenv("OPENSHIFT_AUTH_TOKEN");
		if(authToken == null || authToken.isEmpty()){
			throw new RuntimeException("Cannot auth to openshift: OPENSHIFT_AUTH_TOKEN not found");
		}
		client.getAuthorizationContext().setToken(authToken);
		
		return client;
	}

	private void scaleUp(IClient client){

		String namespace;
		String dcConfig;

		
		namespace = System.getenv("CURRENT_NAMESPACE");
		if(namespace == null || namespace.isEmpty()){
			throw new RuntimeException("Cannot retrieve current namespace: CURRENT_NAMESPACE not found");
		}
		
		// TODO DeploymentConfig should be retrieved dynamically
		dcConfig = System.getenv("hogajama");
		System.out.println("dcConfig: " + dcConfig);

		IDeploymentConfig depconfig = (IDeploymentConfig) client.get(ResourceKind.DEPLOYMENT_CONFIG, dcConfig, namespace);
		
		int replicas = depconfig.getDesiredReplicaCount();
		depconfig.setDesiredReplicaCount(replicas + 1);

		client.update(depconfig);
		
		log.info("Scale up to replica count of: {}", replicas + 1);
	}
}
