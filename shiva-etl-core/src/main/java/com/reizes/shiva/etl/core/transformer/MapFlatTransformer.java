/*
 * @(#)MapFlatTransformer.java $version 2012. 3. 21.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.etl.core.transformer;

import java.util.HashMap;
import java.util.Map;

/**
 * multi depth map을 delimiter로 붙인 이름으로 된 single depth map으로 변환한다.
 * @author reizes
 * @since 2.1.1
 */
public class MapFlatTransformer extends AbstractTransformer {
	private String delimiter = "."; // default는 .

	@SuppressWarnings("unchecked")
	private void flatter(String curKey, Map<String, ?> inputMap, Map<String, Object> flatMap) {
		for (String key : inputMap.keySet()) {
			Object value = inputMap.get(key);

			if (curKey != null) {
				key = curKey + '.' + key;
			}

			if (value instanceof Map) {
				flatter(key, (Map<String, ?>)value, flatMap);
			} else {
				flatMap.put(key, value);
			}
		}
	}

	/**
	 * @param input
	 * @return
	 * @throws Exception
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object doProcess(Object input) throws Exception {
		Map<String, ?> map = (Map<String, ?>)input;
		Map<String, Object> flatMap = new HashMap<String, Object>();

		flatter(null, map, flatMap);

		return flatMap;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}
