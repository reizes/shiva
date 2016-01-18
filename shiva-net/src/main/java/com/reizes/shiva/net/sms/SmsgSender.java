/*
 * @(#)SmsSender.java $version 2012. 8. 6.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.net.sms;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.reizes.shiva.net.http.HttpClientWrapper;

/**
 * 검색개발센터 smsg를 이용해 SMS 및 email을 보내는 클래스
 * 참고 : http://wiki.nhncorp.com/display/lsss/SMSG+User+Manual
 * 사용자 등록 : http://d8e888.naver.com:20202/20080804-kis2u-register/register.php
 * @since 2.1.5
 * @author reizes
 */
public class SmsgSender {
	private static final String SMSG_HOST = "http://ssea.naver.com:20000/smsg"; // SMSG 서버

	public static boolean sendSms(String id, String msg) throws ClientProtocolException, URISyntaxException, IOException {
		return sendSms(id, msg, null);
	}

	public static boolean sendSms(String id, String msg, String link) throws ClientProtocolException, URISyntaxException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("msg", msg);

		if (link != null) {
			params.put("link", link);
		}

		params.put("method", SmsgMethod.SMS.getCode());

		HttpClientWrapper httpClient = new HttpClientWrapper();
		try {
			httpClient.setUrl(SMSG_HOST);
			httpClient.setConnectionTimeout(10000);
			httpClient.setSocketTimeout(10000);
			httpClient.setUrlEncoding("EUC-KR");

			String result = httpClient.executeForString(params);

			if (result != null) {
				return "00".equals(result);
			}

			return false;
		} finally {
			httpClient.close();
		}
	}

	public static boolean sendMail(String id, String subject, String msg) throws ClientProtocolException, URISyntaxException, IOException {
		return sendMail(id, subject, msg, null);
	}

	public static boolean sendMail(String id, String subject, String msg, String link) throws ClientProtocolException, URISyntaxException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		params.put("msg", msg);
		params.put("subject", subject);

		if (link != null) {
			params.put("link", link);
		}

		params.put("method", SmsgMethod.EMAIL.getCode());

		HttpClientWrapper httpClient = new HttpClientWrapper();
		try {
			httpClient.setUrl(SMSG_HOST);
			httpClient.setConnectionTimeout(10000);
			httpClient.setSocketTimeout(10000);
			httpClient.setUrlEncoding("EUC-KR");

			String result = httpClient.executeForString(params);

			if (result != null) {
				return "00".equals(result);
			}

			return false;
		} finally {
			httpClient.close();
		}
	}
}
