package com.gepardec.hogarama.monitoring;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ApplicationPath("/")
public class MetricsRestApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MetricsRestApp.class);
  
    public MetricsRestApp() {
        logger.info("Starting REST application " + this.getClass().getName());
    }

}