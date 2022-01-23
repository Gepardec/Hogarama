package com.gepardec.hogarama.service.couchbase;

import com.couchbase.client.core.diagnostics.PingResult;
import com.couchbase.client.core.diagnostics.PingState;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.core.service.ServiceType;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.*;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
public class CouchbaseProducerIT {

  @Inject
  private CouchbaseProducer classToTest;


  @BeforeEach
  public void setUp() {
    readDummyEntryIfExists(); // Workaround: without reading a dummy entry, the endpoints are not available yet.
  }

  @Test
  public void validate_cluster() {
    Cluster cluster = classToTest.getCluster();
    assertNotNull(cluster);
    verifyPingResult(cluster.ping());
  }

  @Test
  public void validateBucket() {
    Bucket bucket = classToTest.getBucket();
    assertNotNull(bucket);
    assertEquals(CouchbaseProducer.BUCKET_NAME, bucket.name());
    verifyPingResult(bucket.ping());
  }

  @Test
  public void validateScope() {
    Scope scope = classToTest.getScope();
    assertNotNull(scope);
    assertEquals(CouchbaseProducer.SCOPE_NAME, scope.name());
  }


  private void readDummyEntryIfExists() {
    try {
      Collection collection = classToTest.getBucket().defaultCollection();
      collection.get("sensor::test");
    } catch (DocumentNotFoundException ex) {
      // Test-Entry does not exist. Nothing to see here.
    }
  }

  private void verifyPingResult(PingResult pingResult) {
    assertNotEquals(0, pingResult.endpoints().size(), "Available endpoints is 0, likely due to authentication failure");
    PingState pingState = pingResult.endpoints().get(ServiceType.QUERY).get(0).state();
    assertEquals(PingState.OK, pingState, "Expected state to be 'OK'");
  }
}
