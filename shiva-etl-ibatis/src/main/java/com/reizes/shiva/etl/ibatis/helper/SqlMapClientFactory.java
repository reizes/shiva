package com.reizes.shiva.etl.ibatis.helper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

import com.reizes.shiva.utils.ResourceUtil;

/**
 * 공용으로 사용되는 Singleton SqlMapClient를 관리하는 Factory 클래스
 * 2.1.0 - setSqlMap 메소드 추가  , lazy init 하도록 수정
 * @author reizes
 * @since 2009.9.18
 * @since 2.1.0 
 */
public class SqlMapClientFactory {
	protected static Logger log = Logger.getLogger(SqlMapClientFactory.class);
	private static Map<String, SqlMapInfo> map;
	private static String dataSourceMapPath = "datasource-map.properties";

	/**
	 * datasoure-map.properties를 읽고 sql-map을 구성한다. 
	 * @since 2.1.0
	 */
	private static void initDataSourceMap() {
		if (map == null) {
			map = new HashMap<String, SqlMapInfo>();
			Properties properties = new Properties();
			try {
				properties.load(ResourceUtil.getResourceAsStream(dataSourceMapPath));
				log.info("DataSourceMap " + dataSourceMapPath + " loaded...");
			} catch (IOException e) {
				log.warn("DataSourceMap Property File " + dataSourceMapPath + " not exist!");
				return;
			} catch (Exception e) {
				log.error(e);
				return;
			}

			for (String name : properties.stringPropertyNames()) {
				String[] tmp = StringUtils.split((String)properties.get(name), ';');
				try {
					if (tmp.length > 1) {
						addSqlMap(name, tmp[0], tmp[1]);
					} else {
						addSqlMap(name, tmp[0]);
					}
				} catch (IOException e) {
					log.fatal(e);
				}
			}
		}
	}

	/**
	 * Factory에서 SqlMapClient를 생성한다.
	 * @param name - SqlMapClient 이름
	 * @param sqlMapConfigPath - sql-map-config.xml 클래스패스
	 * @param dataSourcePropertyPath - datasource.properties 클래스패스
	 * @throws IOException -
	 */
	public static void addSqlMap(String name, String sqlMapConfigPath, String dataSourcePropertyPath) throws IOException {
		initDataSourceMap();
		map.put(name, new SqlMapInfo(name, sqlMapConfigPath, dataSourcePropertyPath, true));
	}

	/**
	 * Factory에서 SqlMapClient를 생성한다.
	 * @param name - SqlMapClient 이름
	 * @param sqlMapConfigPath - sql-map-config.xml 클래스패스
	 * @throws IOException -
	 */
	public static void addSqlMap(String name, String sqlMapConfigPath) throws IOException {
		initDataSourceMap();
		map.put(name, new SqlMapInfo(name, sqlMapConfigPath, null, true));
	}

	/**
	 * Factory에서 SqlMapClient를 생성한다.
	 * @param sqlMapConfigPath - sql-map-config.xml 클래스패스
	 * @throws IOException -
	 */
	public static void addSqlMap(String sqlMapConfigPath) throws IOException {
		initDataSourceMap();
		map.put(null, new SqlMapInfo(null, sqlMapConfigPath, null, true));
	}

	/**
	 * 이름을 가지고 SqlMapClient를 가져온다. 없으면 NULL 반환
	 * @since 2.1.0
	 * @param name - SqlMapClient 이름
	 * @param adHoc - adhoc client인지 여부
	 * @return - SqlMapClient
	 */
	public static SqlMapClient getSqlMapClient(String name, boolean adHoc) {
		initDataSourceMap();
		SqlMapInfo info = map.get(name);
		try {
			return info != null ? (adHoc ? getAdhocSqlMapClient(info.getSqlMapConfigPath(),
				info.getDataSourcePropertyPath()) : info.getSqlMapClient()) : null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * name 이름으로 sqlMapClient를 설정한다.
	 * @since 2.1.1
	 * @param name - client 이름
	 * @param sqlMapClient - SqlMapClient
	 */
	public static void setSqlMapClient(String name, SqlMapClient sqlMapClient) {
		initDataSourceMap();
		SqlMapInfo info = new SqlMapInfo();
		info.setName(name);
		info.setSingleton(true);
		info.setSqlMapClient(sqlMapClient);
		map.put(name, info);
	}

	/**
	 * 이름을 가지고 SqlMapClient를 가져온다. 없으면 NULL 반환
	 * @param name - SqlMapClient 이름
	 * @return - SqlMapClient
	 */
	public static SqlMapClient getSqlMapClient(String name) {
		return getSqlMapClient(name, false);
	}

	/**
	 * Factory에서 SqlMapClient를 얻는다
	 * @param sqlMapConfigPath - sql-map-config.xml 클래스패스
	 * @param dataSourcePropertyPath - datasource.properties 클래스패스
	 * @return - SqlMapClient
	 * @throws IOException -
	 */
	public static SqlMapClient getAdhocSqlMapClient(String sqlMapConfigPath, String dataSourcePropertyPath) throws IOException {
		initDataSourceMap();
		SqlMapInfo info = new SqlMapInfo(null, sqlMapConfigPath, dataSourcePropertyPath, false);
		return info.getSqlMapClient();
	}

	/**
	 * Factory에서 SqlMapClient를 얻는다
	 * @param sqlMapConfigPath - sql-map-config.xml 클래스패스
	 * @return - SqlMapClient
	 * @throws IOException -
	 */
	public static SqlMapClient getAdhocSqlMapClient(String sqlMapConfigPath) throws IOException {
		initDataSourceMap();
		SqlMapInfo info = new SqlMapInfo(null, sqlMapConfigPath, null, false);
		return info.getSqlMapClient();
	}

	/**
	 * map name, xml path의 값으로 된 map을 받아서 일괄적으로 추가
	 * @since 2.1.0
	 * @param map -
	 * @throws IOException -
	 */
	public void setSqlMap(Map<String, String> map) throws IOException {
		for (String key : map.keySet()) {
			addSqlMap(key, map.get(key));
		}
	}

	/**
	 * map name, SqlMapClient의 값으로 된 map을 세팅
	 * @since 2.1.0
	 * @param map -
	 * @throws IOException -
	 */
	public void setSqlMapClient(Map<String, SqlMapInfo> map) throws IOException {
		SqlMapClientFactory.map = map;
	}

	public static String getDataSourceMapPath() {
		return dataSourceMapPath;
	}

	public static void setDataSourceMapPath(String dataSourceMapPath) {
		SqlMapClientFactory.dataSourceMapPath = dataSourceMapPath;
	}

	/**
	 * @since 2.1.0
	 * @param name
	 * @return
	 */
	public static DataSource getDataSource(String name) {
		return getSqlMapClient(name).getDataSource();
	}

	/**
	 * ibatis sql map에서 query를 가져온다.
	 * @since 2.1.0
	 * @param id - sql map id
	 * @param parameterObject - dynamic sql을 위한 parameter
	 * @return - SQL
	 */
	public static String getSql(SqlMapClient sqlMapClient, String id, Object parameterObject) {
		SqlMapClientImpl client = (SqlMapClientImpl)sqlMapClient;
		MappedStatement mappedStatement = client.getMappedStatement(id);

		//iBatis에서는 ThreadLocal를 통해서 세션을 정의합니다.
		SessionScope sessionScope = new SessionScope();
		StatementScope statementScope = new StatementScope(sessionScope);

		//이부분 매우 중요합니다. 이부분을 생략 하면 다이나믹 쿼리가 적용되지 않습니다.
		mappedStatement.initRequest(statementScope);
		Sql sql = mappedStatement.getSql();

		String query = sql.getSql(statementScope, parameterObject);

		if (parameterObject != null) {
			ParameterMap parameterMap = sql.getParameterMap(statementScope, parameterObject);
			Object[] parameters = parameterMap.getParameterObjectValues(statementScope, parameterObject);

			for (Object parameter : parameters) {
				if (parameter == null) {
					query = StringUtils.replaceOnce(query, "?", "NULL");
				} else {
					if (parameter instanceof Number) {
						query = StringUtils.replaceOnce(query, "?", parameter.toString());
					} else {
						query = StringUtils.replaceOnce(query, "?", "'" + parameter.toString() + "'");
					}
				}
			}
		}

		return query;
	}

	public static String getSql(String dataSourceName, String id, Object parameterObject) {
		return getSql(getSqlMapClient(dataSourceName), id, parameterObject);
	}

	public static String getSql(String dataSourceName, String id) {
		return getSql(dataSourceName, id, null);
	}
}
