package com.gepardec.hogarama.mocks.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class MockCliTest {
	@Test
	public void testCreateRunConfigurationFromArguments() throws Exception {
		String properties = "brokerHost=url" + System.lineSeparator() + 
				"brokerUsername=user" + System.lineSeparator() + 
				"brokerPassword=pwd" + System.lineSeparator() +  
				"brokerTopic=topic";
		String propsFile = writeToTempFile("props.props", properties);
		String messagesFile = writeToTempFile("messages.json", "{1},{2}");
		
		String [] args = new String[] {"-t", messagesFile, "-c", propsFile, "--delayMs", "3000"};
		RunConfiguration runConfiguration = MockCli.createRunConfigurationFromArguments(args);
		
		assertEquals("url", runConfiguration.getHost());
		assertEquals("user", runConfiguration.getUser());
		assertEquals("pwd", runConfiguration.getPassword());
		assertEquals("topic", runConfiguration.getTopic());
		assertEquals(3000L, runConfiguration.getDelayMs());
		assertTrue(Arrays.equals(new String[] {"{1}", "{2}"}, runConfiguration.getMockMessages().toArray()));
		
	}
	
	private static final String writeToTempFile(String filename, String content) throws IOException {
		File tempFile = File.createTempFile(filename, "");
		FileOutputStream tempFileOutputStream = new FileOutputStream(tempFile);
		tempFileOutputStream.write(content.getBytes());
		tempFileOutputStream.close();
		return tempFile.getAbsolutePath();
	}
}
