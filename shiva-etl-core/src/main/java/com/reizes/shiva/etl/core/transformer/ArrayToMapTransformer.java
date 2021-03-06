package com.reizes.shiva.etl.core.transformer;

import java.lang.reflect.Array;
import java.util.HashMap;

import com.reizes.shiva.etl.core.BeforeProcessAware;
import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.context.ProcessContext;

public class ArrayToMapTransformer extends AbstractTransformer implements BeforeProcessAware {

	/*
	 * Array 순서에 따른 맵 이름 배열. NULL이면 skip
	 */
	private String[] mapNames;

	@Override
	public Object doProcess(Object input) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();

		for (int i = 0; i < mapNames.length; i++) {
			if (mapNames[i] != null) {
				map.put(mapNames[i], Array.get(input, i));
			}
		}

		return map;
	}

	public void setMapNames(String[] mapNames) {
		this.mapNames = mapNames;
	}

	@Override
	public void onBeforeProcess(ProcessContext context, Object data) throws Exception {
		if (this.mapNames == null)
			throw new InvalidPropertyException("mapNames property is null!");
	}

}
