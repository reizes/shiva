/*
 * @(#)ServiceRepository.java $version 2010. 6. 8.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.reizes.shiva.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 서비스에서 공유되는 Object Repository
 * 2011.9.7 shiva-webwork로 이동
 * @author reizes
 * @since 2010.1.21
 */
public class ServiceRepository {
	private static Map<String, Object> map = new HashMap<String, Object>();

	public static void clear() {
		map.clear();
	}

	public static boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public static boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public static Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public static Object get(String key) {
		return map.get(key);
	}

	public static boolean isEmpty() {
		return map.isEmpty();
	}

	public static Set<String> keySet() {
		return map.keySet();
	}

	public static Object put(String key, Object value) {
		return map.put(key, value);
	}

	public static void putAll(Map<String, Object> map) {
		map.putAll(map);
	}

	public static Object remove(String key) {
		return map.remove(key);
	}

	public static int size() {
		return map.size();
	}

	public static Collection<Object> values() {
		return map.values();
	}
}
