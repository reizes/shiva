package com.reizes.shiva.etl.dom4j.xmlrpc;

import java.util.Map;

/**
 * XMl-RPC 파라메터 인터페이스 - Model에서 XML-RPC 파라메터를 받기 위한 인터페이스
 * @author reizes
 * @since 2010.1.26
 * @since 2.1.0
 */
public interface XmlRpcParam {
	public Map<String, String> getXmlRpcParamMap();
}
