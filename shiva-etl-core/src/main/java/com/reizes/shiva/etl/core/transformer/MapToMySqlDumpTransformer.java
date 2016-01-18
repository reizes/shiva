/*
 * @(#)NaverDongToDumpTransformer.java $version 2010. 10. 19.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.etl.core.transformer;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.reizes.shiva.utils.StringUtil;

/**
 * @author reizes
 * @since 2010.10.19
 */
public class MapToMySqlDumpTransformer extends AbstractTransformer {
	private String[] columns;

	@SuppressWarnings("unchecked")
	@Override
	public Object doProcess(Object input) throws Exception {
		Map<String, Object> map = (Map<String, Object>)input;
		String[] output = new String[columns.length];

		for (int i = 0; i < columns.length; i++) {
			Object data = map.get(columns[i]);
			output[i] = data != null ? ("\""
				+ StringUtils.replaceEach(data.toString().trim(), new String[] {"\"", "\\", "\t"}, new String[] {
					"\\\"", "\\\\", "\\t"}) + "\"") : "NULL";
		}

		String str = StringUtil.join(output, '\t') + '\n';
		return str;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

}
