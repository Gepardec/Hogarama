package com.gepardec.hogarama.reactive.vo;

public class Event {

  private String key;
  private double value;
  private long id;

  public Event() {
  }

  public Event(String key, double value, long id) {
    this.key = key;
    this.value = value;
    this.id = id;
  }

  public String getKey() {
    return key;
  }

  public double getValue() {
    return value;
  }

  public long getId() {
    return id;
  }

  @SuppressWarnings("unused")
  public void setKey(String key) {
    this.key = key;
  }

  @SuppressWarnings("unused")
  public void setValue(double value) {
    this.value = value;
  }

  @SuppressWarnings("unused")
  public void setId(long id) {
    this.id = id;
  }
}
