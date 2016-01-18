package com.reizes.shiva.repository;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.reizes.shiva.utils.CollectionUtil;
import com.reizes.shiva.utils.ResourceUtil;

/**
 * 서비스 프로퍼티 관리 (캐시도 수행)
 * @author inho, reizes
 * @since 2009-09-07
 * @since 2011-09-07 shiva webwork로 이동
 */
public class ServiceProperties {
	private Properties properties;
	private String[] propertiesNames = {"service.properties"};

	private static ServiceProperties instance;

	/**
	 * @throws IOException -
	 */
	private ServiceProperties() throws IOException {
		properties = new Properties();

		for (String propertiesName : propertiesNames) {
			properties.load(ResourceUtil.getResourceAsStream(propertiesName));
		}
	}

	/**
	 * @return 전문정보 프로퍼티 공통 유틸 싱글톤 클래스
	 * @throws IOException -
	 */
	public static synchronized ServiceProperties getInstance() throws IOException {
		if (instance == null) {
			instance = new ServiceProperties();
		}

		return instance;
	}

	/**
	 * @param key key
	 * @return value
	 * @throws IOException 
	 */
	private String getValue(String key) {
		return properties.getProperty(key);
	}

	public static String get(String key) {
		String value = (String)ServiceRepository.get(key);

		if (value == null) {
			try {
				value = getInstance().getValue(key);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			ServiceRepository.put(key, value);
		}
		return value;
	}

	public static String[] getAsArray(String key, String delim) {
		String[] value = (String[])ServiceRepository.get(key);

		if (value == null) {
			try {
				String data = getInstance().getValue(key);
				value = StringUtils.split(data, delim);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			ServiceRepository.put(key, value);
		}
		return value;
	}

	public static Set<String> getAsSet(String key, String delim) {
		return CollectionUtil.toSet(getAsArray(key, delim));
	}

	public String[] getPropertiesNames() {
		return propertiesNames;
	}

	public void setPropertiesNames(String[] propertiesNames) {
		this.propertiesNames = propertiesNames;
	}

}
