package com.reizes.shiva.net.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * apache http client를 이용하기 위한 wrapper
 * @author reizes
 * @since 2010.2.4
 */
public class HttpClientWrapper {
	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * HTTP 요청 메소드 (GET, POST 2개만 사용)
	 * @author inho
	 */
	public enum HttpMethod {
		GET, POST;
	}

	private String url;
	private HttpMethod method = HttpMethod.GET;
	private int connectionTimeout = 3000;
	private int socketTimeout = 2000;
	private String urlEncoding = "UTF-8";
	private String contentCharSet = "UTF-8";
	private String responseEncoding = "UTF-8";
	private int retryCount = 3;
	private HttpClient httpclient;

	/**
	 * GET 파라메터를 붙여서 URL을 완성한다.
	 * @param url - request url
	 * @param requestParams - Map<String, ? extends Object> parameter
	 * @return - url
	 * @throws URISyntaxException -
	 * @throws UnsupportedEncodingException -
	 */
	private HttpGet getGetMethod(String url, Map<String, ? extends Object> requestParams) throws URISyntaxException,
		UnsupportedEncodingException {
		URI uri = new URI(url);
		boolean first = uri.getQuery() == null || uri.getQuery().length() == 0;

		if (requestParams != null && requestParams.size() > 0) {
			for (String key : requestParams.keySet()) {
				url += first ? '?' : '&';
				first = false;
				url += key + '=' + URLEncoder.encode(requestParams.get(key).toString(), urlEncoding);
			}
		}

		log.debug(url);
		return new HttpGet(url);
	}

	/**
	 * HttpPost에 form parameter 세팅
	 * @param url - url
	 * @param requestParams - 파라메터
	 * @throws UnsupportedEncodingException
	 */
	private HttpPost getPostMethod(String url, Map<String, ?> requestParams) throws UnsupportedEncodingException {
		HttpPost post = new HttpPost(url);

		if (requestParams != null && requestParams.size() > 0) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			for (String key : requestParams.keySet()) {
				formparams.add(new BasicNameValuePair(key, requestParams.get(key).toString()));
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
			entity.setContentEncoding(contentCharSet);
			post.setEntity(entity);
		}

		log.debug(url);
		return post;
	}

	/**
	 * POST, GET Type에 따라 method를 생성하여 반환
	 * @param requestParams - 전달할 파라메터
	 * @return - 메소드
	 * @throws UnsupportedEncodingException -
	 * @throws URISyntaxException -
	 */
	private HttpRequestBase getMethod(Map<String, ?> requestParams) throws UnsupportedEncodingException,
		URISyntaxException {
		if (StringUtils.isBlank(url)) {
			throw new IllegalStateException("URL is blank.");
		}

		HttpRequestBase request;

		if (method.equals(HttpMethod.POST)) {
			request = getPostMethod(url, requestParams);
		} else {
			request = getGetMethod(url, requestParams);
		}

		request.getParams().setParameter("http.socket.timeout", socketTimeout);
		request.getParams().setParameter("http.connection.timeout", connectionTimeout);

		return request;
	}

	/**
	 * 내부 실행 메소드
	 * @param requestParams -
	 * @return -
	 * @throws URISyntaxException -
	 * @throws ClientProtocolException -
	 * @throws IOException -
	 * @throws NoSuchAlgorithmException 
	 */
	private HttpResponse execute(Map<String, ?> requestParams) throws URISyntaxException, ClientProtocolException,
		IOException {
		if (httpclient == null) {
			httpclient = new DefaultHttpClient();
		}

		HttpRequestBase method = getMethod(requestParams);

		HttpResponse response = null;

		for (int i = 0; i < retryCount; i++) {
			try {
				response = httpclient.execute(method);
				break;
			} catch (ConnectTimeoutException ex) {
				log.warn(ex);

				if (i == retryCount - 1) {
					throw ex;
				}
			}
		}

		StatusLine statusLine = response.getStatusLine();

		if (statusLine.getStatusCode() >= 300) {
			throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
		}

		return response;
	}

	/**
	 * HTTP를 호출하고 결과를 Input Stream으로 반환
	 * @param requestParams - 파라메터
	 * @return -
	 * @throws URISyntaxException -
	 * @throws ClientProtocolException -
	 * @throws IOException -
	 */
	public InputStream executeForInputStream(Map<String, ?> requestParams) throws URISyntaxException,
		ClientProtocolException, IOException {
		HttpResponse response = execute(requestParams);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			return (new BufferedHttpEntity(entity)).getContent();
		}

		return null;
	}

	/**
	 * HTTP를 호출하고 결과를 String으로 반환
	 * @param requestParams - 파라메터
	 * @return -
	 * @throws URISyntaxException -
	 * @throws ClientProtocolException -
	 * @throws IOException -
	 */
	public String executeForString(Map<String, ?> requestParams) throws URISyntaxException, ClientProtocolException,
		IOException {
		HttpResponse response = execute(requestParams);
		HttpEntity entity = response.getEntity();

		if (entity != null) {
			return EntityUtils.toString(entity, responseEncoding);
		}

		return null;
	}

	/**
	 * HTTP Connection을 해제
	 */
	public void close() {
		httpclient.getConnectionManager().shutdown();
		httpclient = null;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public String getUrlEncoding() {
		return urlEncoding;
	}

	public void setUrlEncoding(String urlEncoding) {
		this.urlEncoding = urlEncoding;
	}

	public String getContentCharSet() {
		return contentCharSet;
	}

	public void setContentCharSet(String contentCharSet) {
		this.contentCharSet = contentCharSet;
	}

	public String getResponseEncoding() {
		return responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		this.responseEncoding = responseEncoding;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

}
