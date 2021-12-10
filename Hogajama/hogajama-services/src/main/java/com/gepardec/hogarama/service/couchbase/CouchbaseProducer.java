package com.gepardec.hogarama.service.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.gepardec.hogarama.domain.exception.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class CouchbaseProducer {
  private Cluster cluster;
  private Bucket  bucket;
  private Scope scope;
  private Collection collection;

  private static final Logger LOG = LoggerFactory.getLogger(CouchbaseProducer.class);

  private static final String HOST       = System.getenv("COUCHBASE_HOST");
  private static final String BUCKET     = System.getenv("COUCHBASE_BUCKET");
  private static final String SCOPE      = System.getenv("COUCHBASE_SCOPE");
  private static final String COLLECTION = System.getenv("COUCHBASE_COLLECTION");
  private static final String USER       = System.getenv("COUCHBASE_USER");
  private static final String PASSWORD   = System.getenv("COUCHBASE_PASSWORD");

  @PostConstruct
  public void init() {
    try {
      LOG.info("Connecting to Couchbase...");
      cluster = Cluster.connect(HOST, USER, PASSWORD);
      bucket = cluster.bucket(BUCKET);
      scope = bucket.scope(SCOPE);
      collection = scope.collection(COLLECTION);
      LOG.info("Successfully connected to Couchbase.");
    } catch (Exception e) {
      throw new TechnicalException("Failed connecting to Couchbase.", e);
    }
  }

  @Produces
  public Cluster getCluster() {
    return cluster;
  }

  @Produces
  public Bucket getBucket() {
    return bucket;
  }

  @Produces
  public Scope getScope() {
    return scope;
  }

  @Produces
  public Collection getCollection() {
    return collection;
  }

  @PreDestroy
  public void destroy() {
    LOG.info("Disconnecting from Couchbase...");
    if(cluster != null) {
      cluster.disconnect();
      LOG.info("Successfully disconnected from Couchbase.");
    }
  }
}
