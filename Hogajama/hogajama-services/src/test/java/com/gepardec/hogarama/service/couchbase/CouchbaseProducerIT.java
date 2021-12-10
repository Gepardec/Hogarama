package com.gepardec.hogarama.service.couchbase;

import com.couchbase.client.core.diagnostics.PingResult;
import com.couchbase.client.core.diagnostics.PingState;
import com.couchbase.client.core.service.ServiceType;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


public class CouchbaseProducerIT {

  private static CouchbaseProducer classToTest;
  private static Cluster cluster;
  private static Bucket bucket;
  private static Collection collection;


  @BeforeClass
  public static void setUp() {

    classToTest = new CouchbaseProducer();
    classToTest.init();

    cluster = classToTest.getCluster();
    bucket = classToTest.getBucket();
    collection = classToTest.getCollection();
    readDummyEntry(); // Workaround: without reading a dummy entry, there are no endpoints available.
  }

  @AfterClass
  public static void afterClass() {
    classToTest.destroy();
  }

  @Test
  public void validate_cluster() {
    assertNotNull( "Cluster should not be NULL", cluster);
    verifyPingResult(cluster.ping());
  }

  @Test
  public void validateBucket() {
    assertNotNull("Bucket should not be NULL", bucket);
    verifyPingResult(bucket.ping());
  }

  private static void readDummyEntry() {
    collection.get("sensor::test");
  }

  private void verifyPingResult(PingResult pingResult) {
    assertNotEquals("Available endpoints is 0, likely due to authentication failure", 0, pingResult.endpoints().size());
    PingState pingState = pingResult.endpoints().get(ServiceType.QUERY).get(0).state();
    assertEquals("Expected state to be 'OK'", PingState.OK, pingState);
  }
}
