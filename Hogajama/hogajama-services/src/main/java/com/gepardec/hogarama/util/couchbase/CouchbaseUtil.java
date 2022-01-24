package com.gepardec.hogarama.util.couchbase;

public class CouchbaseUtil {

  public static String getKey(String collection, String id) {
    return collection + "::" + id;
  }
}
