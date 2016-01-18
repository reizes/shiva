package com.reizes.shiva.etl.dom4j.xmlrpc;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * XML-RPC를 사용하는 DAO Base
 * @author reizes
 * @since 2010.1.20
 * @since 2.1.0
 */
public class BaseXmlRpcClient {
	protected Logger log = Logger.getLogger(this.getClass());
	private String host; // XML-RPC 서버 주소
	private int timeout; // timeout (단위 : ms)

	public BaseXmlRpcClient() {
	}

	public BaseXmlRpcClient(String host) {
		setHost(host);
	}

	public BaseXmlRpcClient(String host, int timeout) {
		setHost(host);
		setTimeout(timeout);
	}

	private XmlRpcClientWrapper init() {
		try {
			return new XmlRpcClientWrapper(host, timeout);
		} catch (MalformedURLException e) {
			log.error(e);
		}

		return null;
	}

	protected Object call(String methodName, XmlRpcParam param) {
		XmlRpcClientWrapper client = init();
		return client != null ? client.call(methodName, param.getXmlRpcParamMap()) : null;
	}

	protected Object call(String methodName, Map<String, String> param) {
		XmlRpcClientWrapper client = init();
		return client != null ? client.call(methodName, param) : null;
	}

	protected Object call(String methodName, String param) {
		XmlRpcClientWrapper client = init();
		return client != null ? client.call(methodName, param) : null;
	}

	/**
	 * 파라메터 배열로 호출하는 메소드 추가
	 * @param methodName -
	 * @param params - Object[]
	 * @return -
	 * @since 2.1.5
	 */
	protected Object call(String methodName, Object[] params) {
		XmlRpcClientWrapper client = init();
		return client != null ? client.call(methodName, params) : null;
	}

	/**
	 * 파라메터 리스트로 호출하는 메소드 추가
	 * @param methodName -
	 * @param params - List<?>
	 * @return -
	 * @since 2.1.5
	 */
	protected Object call(String methodName, List<?> params) {
		XmlRpcClientWrapper client = init();
		return client != null ? client.call(methodName, params) : null;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
