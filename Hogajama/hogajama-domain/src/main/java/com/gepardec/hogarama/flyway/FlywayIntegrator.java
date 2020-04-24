package com.gepardec.hogarama.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.jdbc.DriverDataSource;
import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Driver;

public class FlywayIntegrator implements Integrator {

    private static final Logger LOG = LoggerFactory.getLogger(FlywayIntegrator.class);

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
        InitialContext ctx = null;
        LOG.info("Starting flyway migration");
        try {
            ctx = new InitialContext();
            DataSource datasource = (DataSource) ctx.lookup("java:jboss/datasources/Hogajama");
            Flyway flyway = Flyway.configure().dataSource(datasource).schemas("hogajama").load();
            flyway.migrate();
            LOG.info("Flyway migration succeeded");
        } catch (Exception e) {
            LOG.error("Flyway migration failed", e);
        }

    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactoryImplementor, SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

    }
}
