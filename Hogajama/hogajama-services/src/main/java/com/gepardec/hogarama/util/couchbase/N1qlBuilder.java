package com.gepardec.hogarama.util.couchbase;

import java.util.Arrays;
import java.util.List;

public class N1qlBuilder {
  private final String bucket;
  private final String scope;
  private List<String> select;
  private boolean distinct;
  private String from;

  public N1qlBuilder(String bucket, String scope) {
    this.bucket = bucket;
    this. scope = scope;
    this.distinct = false;
  }

  public N1qlBuilder select(String... selectArgs) {
    select = Arrays.asList(selectArgs);
    return this;
  }

  public N1qlBuilder distinct() {
    this.distinct = true;
    return this;
  }

  public N1qlBuilder from(String from) {
    if(from == null) {
      throw new IllegalArgumentException("FROM must not be null.");
    }
    this.from = from;
    return this;
  }

  public String build() {
    return new StringBuilder()
        .append("SELECT")
        .append(evaluateDistinct())
        .append(evaluateSelect())
        .append(evaluateFrom())
        .toString();
  }

  private String evaluateFrom() {
    StringBuilder sb = new StringBuilder(" FROM ");
    if(bucket != null) { sb.append(bucket + "."); }
    if(scope != null)  { sb.append(scope  + "."); }
    return sb.append(from).toString();
  }

  private String evaluateSelect() {
    return " " + String.join(", ", select);
  }

  private String evaluateDistinct() {
    return distinct ? " DISTINCT" : "";
  }

}
