/*
 * @(#)Mail.java $version 2010. 9. 27.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.net.mail;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * @author reizes
 * @since 2010.9.27
 */
public class EmailSender {
	private String mailTo; // 수신자 (;로 구분)
	private String host = "127.0.0.1"; // SMTP Hosst
	private int port = 25; // STMP Port
	private String mailFrom; // 발신자

	public static String sendMail(String host, int port, String from, String to, String subject, String text) throws IOException,
		EmailException {

		if (to != null && host != null) {
			String[] emailTo = StringUtils.split(to, ';');

			Email email = new SimpleEmail();
			email.setHostName(host);
			email.setSmtpPort(port);
			email.setFrom(from);

			for (String recv : emailTo) {
				email.addTo(recv);
			}

			email.setSubject(subject);
			email.setMsg(text);
			return email.send();
		}

		return null;
	}

	public static String sendHtmlMail(String host, int port, String from, String to, String subject, String html) throws IOException,
		EmailException {

		if (to != null && host != null) {
			String[] emailTo = StringUtils.split(to, ';');

			MultiPartEmail email = new MultiPartEmail();

			email.setCharset("UTF-8");
			email.setHostName(host);
			email.setSmtpPort(port);
			email.setFrom(from);

			for (String recv : emailTo) {
				email.addTo(recv);
			}

			email.setSubject(subject);
			email.setMsg(html);

			return email.send();
		}

		return null;
	}

	public String getMailTo() {
		return mailTo;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	public String sendMail(String subject, String text) throws IOException, EmailException {
		return EmailSender.sendMail(host, port, mailFrom, mailTo, subject, text);
	}
}
