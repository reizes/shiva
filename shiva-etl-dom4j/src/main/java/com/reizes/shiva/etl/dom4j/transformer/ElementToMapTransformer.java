package com.reizes.shiva.etl.dom4j.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.reizes.shiva.etl.core.transformer.AbstractTransformer;
import com.reizes.shiva.utils.StringUtil;

/**
 * dom4j의 Element를 Map으로 변환하는 Transformer
 * @author reizes
 * @since 2.1.5 - 2012.3.21 하위 element들을 하위 맵으로 생성, property도 생성
 */
public class ElementToMapTransformer extends AbstractTransformer {
	private boolean useCamel = false; // 이름을 camelize 한다
	private boolean lowerCase = true; // useCamel이 false일 때, 모두 소문자로 변환한다
	private String nullString = null; // 2.1.5 - null로 표현할 스트링 2012.3.21
	private boolean enforceNotNull = true; // 2.1.5 - null을 빈스트링으로 표현한다.

	/**
	 * 이름을 규칙에 맞게 정규화
	 * @param name -
	 * @return -
	 */
	private String normalizeName(String name) {
		if (useCamel) {
			name = StringUtil.camelize(name);
		} else if (lowerCase) {
			name = name.toLowerCase();
		}

		return name;
	}

	/**
	 * value 가공 처리
	 * @param value -
	 * @return -
	 */
	private String normalizeValue(String value) {
		if (value != null) {
			if (nullString != null && nullString.equals(value)) {
				return enforceNotNull ? "" : null;
			}

			return value;
		} else {
			return enforceNotNull ? "" : value;
		}
	}

	/**
	 * 결과 map에 데이터를 넣는데, 같은 키의 값이 여러개이면, 리스트로 구성한다.
	 * @param map -
	 * @param key -
	 * @param value -
	 */
	@SuppressWarnings("unchecked")
	private void putToDataMap(Map<String, Object> map, String key, Object value) {
		if (map.containsKey(key)) {
			Object exist = map.get(key);

			if (exist instanceof List) {
				((List<Object>)exist).add(value);
			} else {
				List<Object> list = new ArrayList<Object>();
				list.add(exist);
				list.add(value);
				map.put(key, list);
			}
		} else {
			map.put(key, value);
		}
	}

	/**
	 * element를 재귀적으로 탐색하며 map을 생성한다.
	 * @param element -
	 * @return -
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> elementToMap(Element element) {
		Map<String, Object> map = new HashMap<String, Object>();

		// processing attributes
		List<Attribute> attrs = element.attributes();

		for (Attribute attr : attrs) {
			String name = attr.getName();
			String value = attr.getText();
			//attr.detach();

			map.put(normalizeName(name), normalizeValue(value));
		}

		attrs.clear();

		// processing child elements
		List<Element> elements = element.elements();

		for (Element el : elements) {
			String name = normalizeName(el.getName());
			Map<String, Object> subMap = elementToMap(el);

			if (subMap != null && subMap.size() > 0) {
				putToDataMap(map, name, subMap);
			} else {
				String value = el.getTextTrim();

				putToDataMap(map, name, normalizeValue(value));
			}

			el.detach();
		}

		elements.clear();

		return map;
	}

	/**
	 * dom4j의 Element를 Map로 변환하는 Transformer
	 * @param input - Element
	 * @return - Model
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		return elementToMap((Element)input);
	}

	public boolean isUseCamel() {
		return useCamel;
	}

	public void setUseCamel(boolean useCamel) {
		this.useCamel = useCamel;
	}

	public boolean isLowerCase() {
		return lowerCase;
	}

	public void setLowerCase(boolean lowerCase) {
		this.lowerCase = lowerCase;
	}

	public String getNullString() {
		return nullString;
	}

	public void setNullString(String nullString) {
		this.nullString = nullString;
	}

	public boolean isEnforceNotNull() {
		return enforceNotNull;
	}

	public void setEnforceNotNull(boolean enforceNotNull) {
		this.enforceNotNull = enforceNotNull;
	}

}
