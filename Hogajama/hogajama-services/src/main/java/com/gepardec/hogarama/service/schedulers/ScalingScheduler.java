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

@Startup
@Singleton
public class ScalingScheduler {

	private MBeanServer mBeanServer = null;

	@Inject
	private Logger log;
	
	@Schedule(hour = "*", minute = "*", second = "*", info = "Every second")
	public void checkSessions() {
		log.info("Current Acitve Sessions: {}", getActiveSession());
		//TODO: tell openshift to start another pod 
	}

	private int getActiveSession() {

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
}
