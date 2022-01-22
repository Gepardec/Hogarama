package com.gepardec.hogarama.service.couchbase;

import com.couchbase.client.core.diagnostics.PingResult;
import com.couchbase.client.core.diagnostics.PingState;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.service.ServiceType;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CouchbaseProducerIT {

  private static CouchbaseProducer classToTest;
  private static Cluster cluster;
  private static Bucket bucket;
  private static Collection collection;


  @BeforeAll
  public static void setUp() {

    classToTest = new CouchbaseProducer();
    classToTest.init();

    cluster = classToTest.getCluster();
    bucket = classToTest.getBucket();
    collection = classToTest.getCollection();
    readDummyEntryIfExists(); // Workaround: without reading a dummy entry, there are no endpoints available.
  }

  @AfterAll
  public static void afterClass() {
    classToTest.destroy();
  }

  @Test
  public void validate_cluster() {
    assertNotNull(cluster, "Cluster should not be NULL");
    verifyPingResult(cluster.ping());
  }

  @Test
  public void validateBucket() {
    assertNotNull(bucket, "Bucket should not be NULL");
    verifyPingResult(bucket.ping());
  }

  private static void readDummyEntryIfExists() {
    try {
      collection.get("sensor::test");
    } catch (DocumentNotFoundException ex) {
      // Test-Entry does not exist. Nothing to see here.
    }
  }

  private void verifyPingResult(PingResult pingResult) {
    assertNotEquals( 0, pingResult.endpoints().size(), "Available endpoints is 0, likely due to authentication failure");
    PingState pingState = pingResult.endpoints().get(ServiceType.QUERY).get(0).state();
    assertEquals(PingState.OK, pingState, "Expected state to be 'OK'");
  }
}
