package com.gepardec.hogarama.mocks.cli;

import java.util.List;

public class RunConfiguration {
	public static final String BROKER_HOST = "brokerHost";
	public static final String BROKER_USERNAME = "brokerUsername";
	public static final String BROKER_PASSWORD = "brokerPassword";
	public static final String BROKER_TOPIC = "brokerTopic";
	public static final String PATH_TO_TEST_DATA = "testData";
	public static final String DELAY_MS = "delayMs";
	public static final String PATH_TO_CONFIG = "configuration";

	private String host;
	private String user;
	private String password;
	private String topic;
	private long delayMs;
	private List<String> mockMessages;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public long getDelayMs() {
		return delayMs;
	}

	public void setDelayMs(long delayMs) {
		this.delayMs = delayMs;
	}

	public List<String> getMockMessages() {
		return mockMessages;
	}

	public void setMockMessages(List<String> mockMessages) {
		this.mockMessages = mockMessages;
	}

}
