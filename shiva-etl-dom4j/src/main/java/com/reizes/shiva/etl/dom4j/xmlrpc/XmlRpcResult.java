package com.reizes.shiva.etl.dom4j.xmlrpc;

import java.util.Map;

/**
 * XMl-RPC 결과 데이터 인터페이스 - 결과 Map을 Model로 변환하기 위한 메소드
 * @author reizes
 * @since 2010.1.26
 * @since 2.1.0
 */
public interface XmlRpcResult {
	public void valueOf(Map<String, ?> result) throws Exception;
}
