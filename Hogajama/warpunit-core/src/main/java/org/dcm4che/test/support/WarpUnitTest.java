package org.dcm4che.test.support;

import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class WarpUnitTest {

    protected WarpMeta warpMeta;

    private static final Logger LOG = LoggerFactory.getLogger(WarpUnitTest.class);

    private WarpGate gate;

    @Inject
    private JPASupport jpaSupport;

    private WarpUnitTestConfig config;

    private List<Runnable> cleanUpHandlers = new ArrayList<>();

    @After
    public void tearDown() {
        for (Runnable cleanUpHandler : cleanUpHandlers) {
            try {
                cleanUpHandler.run();
            } catch (Exception e) {
                LOG.warn("Exception occured while running cleanup task.", e);
            }
        }
    }

    @Before
    public void setup() {
        warpMeta = new WarpMeta();
        config = getConfig();
        gate = WarpUnit.builder()
                .primaryClass(this.getClass())
                .moreClasses(WarpUnitTest.class, WarpUnitTestConfig.class)
                .url(config.baseRestUrl())
                .createGate();
    }

    protected void cleanUp(Runnable cleanUpRunner) {
        cleanUpHandlers.add(cleanUpRunner);
    }

    protected EntityManipulator getEntityManipulator() {
        return new EntityManipulator(config.baseRestUrl());
    }

    protected void warp(Runnable lambda) {
        gate.warp(warpMeta, lambda);
    }

    protected <R> R warp(Supplier<R> lambda) {
        return gate.warp(warpMeta, lambda);
    }

    private WarpUnitTestConfig getConfig() {
        if (this.getClass().isAnnotationPresent(WarpUnitTestConfig.class)) {
            return this.getClass().getAnnotation(WarpUnitTestConfig.class);
        } else {
            throw new RuntimeException("WarpUnitTestConfig on the test not found");
        }
    }

}
