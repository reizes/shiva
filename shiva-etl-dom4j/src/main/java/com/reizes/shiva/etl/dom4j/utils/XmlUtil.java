package com.reizes.shiva.etl.dom4j.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

/**
 * XML 관련 유틸리티
 * @author reizes
 * @since 2010.2.4
 * @since 2.1.0
 */
public class XmlUtil {

	/**
	 * InputStream으로 부터 dom4j의 Document를 반환
	 * @param is - InputStream
	 * @param encoding - encoding
	 * @return - Document
	 * @throws SAXException -
	 * @throws UnsupportedEncodingException -
	 * @throws DocumentException -
	 */
	public static Document getXmlDocumentFromInputStream(InputStream is, String encoding) throws SAXException,
		UnsupportedEncodingException, DocumentException {
		SAXReader saxReader = new SAXReader();
		saxReader.setFeature("http://apache.org/xml/features/allow-java-encodings", true);

		return saxReader.read(new InputStreamReader(is, encoding));
	}

	/**
	 * 2011.5.13 child node가 있는 경우 separator로 붙여서 준다. 아니면 현재 노드의 값을 map으로 리턴한다.
	 * @since 2.1.2
	 * @param el
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, String> getMapFromElement(Element el, String parentName, String separator) {
		Map<String, String> map = new HashMap<String, String>();

		List<Element> childList = el.elements();

		if (childList != null && childList.size() > 0) {
			for (Element childEl : childList) {
				map.putAll(getMapFromElement(childEl, (parentName != null ? (parentName + separator) : "") + el.getName(), separator));
			}
		} else {
			map.put((parentName != null ? (parentName + separator) : "") + el.getName(), el.getTextTrim());
		}

		return map;
	}

	/**
	 * XPath가 가리키는 xml을  Map으로 만든다. 
	 * @since 2.1.2
	 * @param doc - Document
	 * @param xPathExpression - XPath
	 * @param separator - child node가 있을 경우 이름 사이를 구분하기 위한 구분자
	 * @param namespaceMap - NameSpace Map
	 * @return - Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getMapFromFlatXml(Node doc, String xPathExpression, String separator, Map<String, String> namespaceMap) {
		Map<String, String> map = new HashMap<String, String>();
		XPath xpath = doc.createXPath(xPathExpression);

		if (namespaceMap != null) {
			xpath.setNamespaceURIs(namespaceMap);
		}

		List<Node> list = xpath.selectNodes(doc);

		if (list != null && list.size() > 0) {
			for (Node node : list) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					map.putAll(getMapFromElement((Element)node, null, separator == null ? "" : separator));
				}
			}

			return map;
		}

		return null;
	}

	/**
	 * XPath가 가리키는 flat xml (child가 없는)을 Map으로 만든다. 
	 * @param doc - Document
	 * @param xPathExpression - XPath
	 * @param namespaceMap - NameSpace Map
	 * @return - Map
	 */
	public static Map<String, String> getMapFromFlatXml(Node doc, String xPathExpression, Map<String, String> namespaceMap) {
		return getMapFromFlatXml(doc, xPathExpression, null, namespaceMap);
	}

	/**
	 * XPath가 가리키는 flat xml (child가 없는)을 Map으로 만든다. 
	 * @param doc - Document
	 * @param xPathExpression - XPath
	 * @param separator - child node가 있을 경우 이름 사이를 구분하기 위한 구분자
	 * @return - Map
	 */
	public static Map<String, String> getMapFromFlatXml(Node doc, String xPathExpression, String separator) {
		return getMapFromFlatXml(doc, xPathExpression, separator, null);
	}

	/**
	 * XPath가 가리키는 flat xml (child가 없는)을 Map으로 만든다. 
	 * @param doc - Document
	 * @param xPathExpression - XPath
	 * @return - Map
	 */
	public static Map<String, String> getMapFromFlatXml(Node doc, String xPathExpression) {
		return getMapFromFlatXml(doc, xPathExpression, (Map<String, String>)null);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getMapListFromFlatXml(Node doc, String xPathExpression, String separator, Map<String, String> namespaceMap) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		XPath xpath = doc.createXPath(xPathExpression);

		if (namespaceMap != null) {
			xpath.setNamespaceURIs(namespaceMap);
		}

		List<Node> nodes = xpath.selectNodes(doc);

		if (nodes != null && nodes.size() > 0) {
			for (Node node : nodes) {
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					list.add(getMapFromFlatXml(node, "./*", separator, namespaceMap));
				}
			}

			return list;
		}

		return null;
	}

	public static List<Map<String, String>> getMapListFromFlatXml(Node doc, String xPathExpression, Map<String, String> namespaceMap) {
		return getMapListFromFlatXml(doc, xPathExpression, null, namespaceMap);
	}

	public static List<Map<String, String>> getMapListFromFlatXml(Node doc, String xPathExpression, String separator) {
		return getMapListFromFlatXml(doc, xPathExpression, separator, null);
	}

	public static List<Map<String, String>> getMapListFromFlatXml(Node doc, String xPathExpression) {
		return getMapListFromFlatXml(doc, xPathExpression, (Map<String, String>)null);
	}
}
