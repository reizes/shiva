/*
 * @(#)SmsgSenderTest.java $version 2012. 8. 6.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.net.sms;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author reizes
 */
public class SmsgSenderTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.reizes.shiva.net.sms.SmsgSender#sendSms(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testSendSmsStringString() throws ClientProtocolException, URISyntaxException, IOException {
		assertTrue(SmsgSender.sendSms("reizes", "test1"));
	}

	/**
	 * Test method for {@link com.reizes.shiva.net.sms.SmsgSender#sendSms(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testSendSmsStringStringString() throws ClientProtocolException, URISyntaxException, IOException {
		assertTrue(SmsgSender.sendSms("reizes", "test2", "http://www.naver.com"));
	}

	/**
	 * Test method for {@link com.reizes.shiva.net.sms.SmsgSender#sendMail(java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testSendMailStringStringString() throws ClientProtocolException, URISyntaxException, IOException {
		assertTrue(SmsgSender.sendMail("reizes", "test3", "test3 - email msg"));
	}

	/**
	 * Test method for {@link com.reizes.shiva.net.sms.SmsgSender#sendMail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testSendMailStringStringStringString() throws ClientProtocolException, URISyntaxException, IOException {
		assertTrue(SmsgSender.sendMail("reizes", "test3", "test3 - email msg", "http://www.naver.com"));
	}
}
