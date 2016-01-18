package com.reizes.shiva.etl.dom4j.xmlrpc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * XML-RPC Client Wrapper
 * 단일 object를 파라메터로 받고, 단일 Object를 return 하는 method call wrapper
 * @author reizes
 * @since 2010.1.20
 * @since 2.1.0
 */
public class XmlRpcClientWrapper {
	protected Logger log = Logger.getLogger(this.getClass());
	private XmlRpcClient client;

	public XmlRpcClientWrapper(String serverUrl) throws MalformedURLException {
		init(serverUrl, null);
	}

	public XmlRpcClientWrapper(String serverUrl, Integer connectionTimeout) throws MalformedURLException {
		init(serverUrl, connectionTimeout);
	}

	private void init(String serverUrl, Integer connectionTimeout) throws MalformedURLException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(serverUrl));
		config.setReplyTimeout(20000);

		if (connectionTimeout != null) {
			config.setConnectionTimeout(connectionTimeout);
		}

		client = new XmlRpcClient();
		client.setConfig(config);
	}

	/**
	 * 파라메터 배열을 가지고 call 하는 메소드 추가
	 * @param methodName -
	 * @param param - 
	 * @return -
	 * @since 2.1.5
	 */
	public Object call(String methodName, Object param) {
		Object result = null;
		try {
			log.info("XML RPC " + methodName + " call : " + param);

			if (param.getClass().isArray()) {
				result = client.execute(methodName, (Object[])param);
			} else if (param instanceof List) {
				result = client.execute(methodName, (List<?>)param);
			} else {
				result = client.execute(methodName, new Object[] {param});
			}

			if (result instanceof Object[]) {
				for (Object obj : (Object[])result) {
					log.info("XML RPC " + methodName + " result : " + obj);
				}
			} else {
				log.info("XML RPC " + methodName + " result : " + result);
			}

			return result;
		} catch (XmlRpcException e) {
			log.fatal(e);
			return null;
		}
	}
}
