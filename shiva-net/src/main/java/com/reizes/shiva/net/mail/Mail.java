/*
 * @(#)Mail.java $version 2011. 12. 6.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.net.mail;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;

/**
 * apache mail을 사용하지 않는 자체 mail class (한글이 깨지지 않음)
 * @author reizes
 */
public class Mail {
	private String host = "127.0.0.1";
	private int port = 25;
	private boolean debug = false;
	private boolean auth = false;
	private String id;
	private String password;
	private String charset = "utf-8";

	private static final String HTML_MESSAGE_START = "<html><body><pre>";
	private static final String HTML_MESSAGE_END = "</pre></body></html>";

	/**
	* SMTP Authenticator
	*/
	private final class MailAuthenticator extends javax.mail.Authenticator {
		private String id;
		private String pw;

		public MailAuthenticator(String id, String pw) {
			this.id = id;
			this.pw = pw;
		}

		protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
			return new javax.mail.PasswordAuthentication(id, pw);
		}

	}

	public Session getSessoin() {
		Properties prop = new Properties();

		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		prop.put("mail.debug", debug ? "true" : "false");

		// SMTP 서버 인증정보
		prop.put("mail.smtp.auth", auth ? "true" : "false");

		if (auth) {
			MailAuthenticator auth = new MailAuthenticator(id, password);
			return Session.getInstance(prop, auth);
		}

		return Session.getInstance(prop);
	}

	public void sendMail(String fromName, String from, String to, String cc, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(fromName, from, to, cc, null, subject, Mail.HTML_MESSAGE_START + content + Mail.HTML_MESSAGE_END);
	}

	public void sendMail(String fromName, String from, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(fromName, from, to, null, null, subject, Mail.HTML_MESSAGE_START + content + Mail.HTML_MESSAGE_END);
	}

	public void sendMail(String from, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(null, from, to, null, null, subject, Mail.HTML_MESSAGE_START + content + Mail.HTML_MESSAGE_END);
	}

	public void sendMail(String fromName, String from, String to, String cc, String bcc, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(fromName, from, to, cc, bcc, subject, Mail.HTML_MESSAGE_START + content + Mail.HTML_MESSAGE_END);
	}

	public void sendHtmlMail(String fromName, String from, String to, String cc, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(fromName, from, to, cc, null, subject, Mail.HTML_MESSAGE_START + content + Mail.HTML_MESSAGE_END);
	}

	public void sendHtmlMail(String fromName, String from, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(fromName, from, to, null, null, subject, content);
	}

	public void sendHtmlMail(String from, String to, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		sendHtmlMail(null, from, to, null, null, subject, content);
	}

	private InternetAddress[] parseAddresses(String input) throws AddressException {
		return InternetAddress.parse(StringUtils.replace(input, ";", ","));
	}

	public void sendHtmlMail(String fromName, String from, String to, String cc, String bcc, String subject, String content) throws UnsupportedEncodingException, MessagingException {
		boolean parseStrict = false;
		MimeMessage message = new MimeMessage(getSessoin());
		InternetAddress address = InternetAddress.parse(from, parseStrict)[0];

		if (fromName != null) {
			address.setPersonal(fromName, charset);
		}

		message.setFrom(address);

		message.setRecipients(Message.RecipientType.TO, parseAddresses(to));

		if (cc != null) {
			message.setRecipients(Message.RecipientType.CC, parseAddresses(cc));
		}
		if (bcc != null) {
			message.setRecipients(Message.RecipientType.BCC, parseAddresses(bcc));
		}

		message.setSubject(subject, charset);

		message.setHeader("X-Mailer", "sendMessage");
		message.setSentDate(new java.util.Date()); // 보낸 날짜 

		Multipart multipart = new MimeMultipart();
		MimeBodyPart bodypart = new MimeBodyPart();
		bodypart.setContent(content, "text/html; charset=" + charset);
		multipart.addBodyPart(bodypart);

		message.setContent(multipart);
		Transport.send(message);
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

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isAuth() {
		return auth;
	}

	public void setAuth(String id, String password) {
		this.auth = true;
		this.id = id;
		this.password = password;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
