/*
 * @(#)SqlMapInfo.java $version 2010. 10. 19.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.etl.ibatis.helper;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.utils.ResourceUtil;

/**
 * @author reizes
 * @since 2.1.0
 */
public class SqlMapInfo {
	private String name;
	private String sqlMapConfigPath;
	private String dataSourcePropertyPath;
	private boolean isSingleton;
	private SqlMapClient sqlMapClient;

	public SqlMapInfo() {

	}

	public SqlMapInfo(String name, String sqlMapConfigPath, String dataSourcePropertyPath, boolean isSingleton) {
		setName(name);
		setSqlMapConfigPath(sqlMapConfigPath);
		setDataSourcePropertyPath(dataSourcePropertyPath);
		setSingleton(isSingleton);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSqlMapConfigPath() {
		return sqlMapConfigPath;
	}

	public void setSqlMapConfigPath(String sqlMapConfigPath) {
		this.sqlMapConfigPath = sqlMapConfigPath;
	}

	public String getDataSourcePropertyPath() {
		return dataSourcePropertyPath;
	}

	public void setDataSourcePropertyPath(String dataSourcePropertyPath) {
		this.dataSourcePropertyPath = dataSourcePropertyPath;
	}

	public boolean isSingleton() {
		return isSingleton;
	}

	public void setSingleton(boolean isSingleton) {
		this.isSingleton = isSingleton;
	}

	/**
	 * 현재 정보로 SqlMapClient를 생성
	 * @return -
	 * @throws IOException -
	 */
	public SqlMapClient getSqlMapClient() throws IOException {
		if (sqlMapClient == null) {
			if (name == null) {
				name = "master";
			}

			if (sqlMapConfigPath == null) {
				throw new InvalidPropertyException("sqlMapConfigPath is null");
			}

			Reader reader = Resources.getResourceAsReader(sqlMapConfigPath);

			SqlMapClient sqlMapClient;

			if (dataSourcePropertyPath == null) {
				sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
			} else {
				Properties prop = new Properties();
				prop.load(ResourceUtil.getResourceAsStream(dataSourcePropertyPath));
				sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader, prop);
			}

			reader.close();

			this.sqlMapClient = sqlMapClient;
		}

		return this.sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}
}
