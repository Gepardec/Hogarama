package com.gepardec.hogarama.domain.unitmanagement.service;

import org.dcm4che.test.support.WarpUnitTest;
import org.dcm4che.test.support.WarpUnitTestConfig;

import java.io.Serializable;

// TODO PH & EE how to define baseRestUrl
@WarpUnitTestConfig(baseRestUrl = "http://localhost:8080/hogajama-rs/rest/")
public class SensorServiceIT extends WarpUnitTest implements Serializable {

    // TODO PH take over int tests as soon as CI process is defined
}
