package com.reizes.shiva.net.http;


import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class HttpClientWrapperTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExecuteForInputStream() throws ClientProtocolException, URISyntaxException, IOException {
		HttpClientWrapper client = new HttpClientWrapper();
		client.setUrl("http://www.naver.com/");
		InputStream result = client.executeForInputStream(null);
		System.out.println(result);
		assertNotNull(result);
		result.close();
	}

	@Test
	public void testExecuteForString() throws ClientProtocolException, URISyntaxException, IOException {
		HttpClientWrapper client = new HttpClientWrapper();
		client.setUrl("http://www.naver.com/");
		String result = client.executeForString(null);
		System.out.println(result);
		assertNotNull(result);
	}

}
