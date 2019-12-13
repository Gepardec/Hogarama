package org.dcm4che.test.support;

import org.dcm4che.test.em.DBAssist;
import org.dcm4che.test.em.DBAssistImpl;
import org.dcm4che.test.em.EntityManipulator;
import org.dcm4che.test.remote.WarpGate;
import org.dcm4che.test.remote.WarpUnit;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.junit.Before;

import javax.inject.Inject;
import java.util.function.Supplier;

public class WarpUnitTest {

    private WarpGate gate;

    @Inject
    private JPASupport jpaSupport;

    private WarpUnitTestConfig config;

    @Before
    public void setup(){
        config = getConfig();
        gate = WarpUnit.builder()
                .primaryClass(this.getClass())
                .moreClasses(WarpUnitTest.class, WarpUnitTestConfig.class)
                .url(config.baseRestUrl())
                .createGate();
    }

    protected EntityManipulator getEntityManipulator(){
        return new EntityManipulator(config.baseRestUrl());
    }

    protected void warp(Runnable lambda){
        gate.warp(lambda);
    }

    protected <R> R warp(Supplier<R> lambda){
        return gate.warp(lambda);
    }

    private WarpUnitTestConfig getConfig(){
        if(this.getClass().isAnnotationPresent(WarpUnitTestConfig.class)){
            return this.getClass().getAnnotation(WarpUnitTestConfig.class);
        } else {
            throw new RuntimeException("WarpUnitTestConfig on the test not found");
        }
    }

}
