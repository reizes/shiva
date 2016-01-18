/*
 * @(#)CharUtil.java $version 2010. 10. 21.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 캐릭터 관련 유틸
 * @author reizes
 */
public class CharUtil {
	private static final Pattern PATTERN_KO = Pattern.compile("[\u1100-\u11FF\u3130-\u318F\uAC00-\uD7AF]");
	private static final Pattern PATTERN_ZH = Pattern.compile("[\u2E80-\u2EFF\u31C0-\u31EF\u3400-\u4DBF\u4E00-\u9FBF\uF900-\uFAFF]");

	public static boolean containsHanja(String data) {
		Matcher matcher = PATTERN_ZH.matcher(data);
		return matcher.find();
	}

	public static boolean containsHangul(String data) {
		Matcher matcher = PATTERN_KO.matcher(data);
		return matcher.find();
	}

	public static boolean isHanja(char ch) {
		return ((ch >= '\u2E80' && ch <= '\u2EFF') || (ch >= '\u31C0' && ch <= '\u31EF')
			|| (ch >= '\u3400' && ch <= '\u4DBF') || (ch >= '\u4E00' && ch <= '\u9FBF') || (ch >= '\uF900' && ch <= '\uFAFF'));
	}

	public static boolean isHangul(char ch) {
		return ((ch >= '\u1100' && ch <= '\u11FF') || (ch >= '\u3130' && ch <= '\u318F') || (ch >= '\uAC00' && ch <= '\uD7AF'));
	}
}
