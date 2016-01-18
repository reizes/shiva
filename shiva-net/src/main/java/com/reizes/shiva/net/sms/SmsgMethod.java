/*
 * @(#)Method.java $version 2012. 8. 6.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.net.sms;

/**
 * smsg method
 * @author reizes
 */
public enum SmsgMethod {
	SMS("sms"),
	EMAIL("email");

	private final String code;

	private SmsgMethod(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static SmsgMethod fromCode(String code) {
		for (SmsgMethod method : SmsgMethod.values()) {
			if (method.getCode().equalsIgnoreCase(code)) {
				return method;
			}
		}

		return null;
	}
}
