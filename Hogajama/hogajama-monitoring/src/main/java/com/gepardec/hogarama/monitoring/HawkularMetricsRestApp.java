package com.gepardec.hogarama.monitoring;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationPath("/")
public class HawkularMetricsRestApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(HawkularMetricsRestApp.class);
  
    public HawkularMetricsRestApp() {
        logger.info("Starting REST application " + this.getClass().getName());
    }

}