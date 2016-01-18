package com.reizes.shiva.etl.ibatis.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;
import com.ibatis.sqlmap.engine.execution.BatchException;

import com.reizes.shiva.etl.ibatis.helper.SqlMapClientFactory;

/**
 * 베이스 DAO 클래스
 * @author reizes
 * @since 2009.9.18
 * @since 2.1.1 - 2011.5.30 중첩 트랜잭션의 경우 reference counter를 사용해서 start, end transaction을 한다.
 * 
 * 2011.3.16 master/slave 분기 기능 제공
 */
public class BaseDao {
	protected Logger log = Logger.getLogger(SqlMapClientFactory.class);

	private SqlMapClient sqlMapClient;
	private SqlMapClient sqlMapClientSlave;
	private boolean forceMaster = false;
	private static final ThreadLocal<Map<SqlMapClient, Integer>> onTransactionRefCntMap = new ThreadLocal<Map<SqlMapClient, Integer>>();

	public BaseDao() {
		this.setSqlMapClient(SqlMapClientFactory.getSqlMapClient("master"));
	}

	public BaseDao(SqlMapClient sqlMapClient) {
		this.setSqlMapClient(sqlMapClient);
	}

	public BaseDao(SqlMapClient sqlMapClient, SqlMapClient sqlMapClientSlave) {
		this.setSqlMapClient(sqlMapClient);
		this.setSqlMapClientSlave(sqlMapClientSlave);
	}

	public void setDataSourceName(String name) {
		this.setSqlMapClient(SqlMapClientFactory.getSqlMapClient(name));
	}

	public void setDataSourceName(String name, String slaveName) {
		this.setSqlMapClient(sqlMapClient = SqlMapClientFactory.getSqlMapClient(name));
		this.setSqlMapClientSlave(SqlMapClientFactory.getSqlMapClient(slaveName));
	}

	private void setTransactionRefCnt(SqlMapClient sqlMapClient, int count) {
		Map<SqlMapClient, Integer> refCntMap = onTransactionRefCntMap.get();

		if (refCntMap == null) {
			refCntMap = new HashMap<SqlMapClient, Integer>();
			onTransactionRefCntMap.set(refCntMap);
		}

		refCntMap.put(sqlMapClient, count);
	}

	private int getTransactionRefCnt(SqlMapClient sqlMapClient) {
		Map<SqlMapClient, Integer> refCntMap = onTransactionRefCntMap.get();

		if (refCntMap == null) {
			refCntMap = new HashMap<SqlMapClient, Integer>();
			onTransactionRefCntMap.set(refCntMap);
			refCntMap.put(sqlMapClient, 0);
		}
		
		Integer refCnt = refCntMap.get(sqlMapClient);

		return refCnt==null?0:refCnt;
	}

	/**
	 * 
	 * @param arg0 - name + query id
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(java.lang.String)
	 */
	public int delete(String arg0) throws SQLException {
		return sqlMapClient.delete(arg0);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#delete(java.lang.String, java.lang.Object)
	 */
	public int delete(String arg0, Object arg1) throws SQLException {
		return sqlMapClient.delete(arg0, arg1);
	}

	/**
	 * 
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#executeBatch()
	 */
	public int executeBatch() throws SQLException {
		return sqlMapClient.executeBatch();
	}

	/**
	 * 
	 * @return -
	 * @throws SQLException -
	 * @throws BatchException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#executeBatchDetailed()
	 */
	public List<?> executeBatchDetailed() throws SQLException, BatchException {
		return sqlMapClient.executeBatchDetailed();
	}

	/**
	 * 
	 * @param arg0 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(java.lang.String)
	 */
	public Object insert(String arg0) throws SQLException {
		return sqlMapClient.insert(arg0);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#insert(java.lang.String, java.lang.Object)
	 */
	public Object insert(String arg0, Object arg1) throws SQLException {
		return sqlMapClient.insert(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String)
	 */
	public List<?> queryForList(String arg0) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForList(arg0);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, java.lang.Object)
	 */
	public List<?> queryForList(String arg0, Object arg1) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForList(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, int, int)
	 */
	public List<?> queryForList(String arg0, int arg1, int arg2) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForList(arg0, arg1, arg2);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @param arg3 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForList(java.lang.String, java.lang.Object, int, int)
	 */
	public List<?> queryForList(String arg0, Object arg1, int arg2, int arg3) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForList(arg0, arg1, arg2, arg3);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(java.lang.String, java.lang.Object, java.lang.String)
	 */
	public Map<?, ?> queryForMap(String arg0, Object arg1, String arg2) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForMap(arg0, arg1, arg2);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @param arg3 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForMap(java.lang.String, java.lang.Object, java.lang.String, java.lang.String)
	 */
	public Map<?, ?> queryForMap(String arg0, Object arg1, String arg2, String arg3) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForMap(arg0, arg1, arg2, arg3);
	}

	/**
	 * 
	 * @param arg0 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String)
	 */
	public Object queryForObject(String arg0) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForObject(arg0);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String, java.lang.Object)
	 */
	public Object queryForObject(String arg0, Object arg1) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForObject(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryForObject(java.lang.String, java.lang.Object, java.lang.Object)
	 */
	public Object queryForObject(String arg0, Object arg1, Object arg2) throws SQLException {
		return (forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryForObject(arg0, arg1, arg2);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(java.lang.String, com.ibatis.sqlmap.client.event.RowHandler)
	 */
	public void queryWithRowHandler(String arg0, RowHandler arg1) throws SQLException {
		(forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryWithRowHandler(arg0, arg1);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @param arg2 -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#queryWithRowHandler(java.lang.String, java.lang.Object, com.ibatis.sqlmap.client.event.RowHandler)
	 */
	public void queryWithRowHandler(String arg0, Object arg1, RowHandler arg2) throws SQLException {
		(forceMaster ? sqlMapClient : (sqlMapClientSlave != null ? sqlMapClientSlave : sqlMapClient)).queryWithRowHandler(arg0, arg1, arg2);
	}

	/**
	 * 
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#startBatch()
	 */
	public void startBatch() throws SQLException {
		sqlMapClient.startBatch();
	}

	/**
	 * 
	 * @param arg0 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(java.lang.String)
	 */
	public int update(String arg0) throws SQLException {
		return sqlMapClient.update(arg0);
	}

	/**
	 * 
	 * @param arg0 -
	 * @param arg1 -
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapExecutor#update(java.lang.String, java.lang.Object)
	 */
	public int update(String arg0, Object arg1) throws SQLException {
		return sqlMapClient.update(arg0, arg1);
	}

	/**
	 * 
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#commitTransaction()
	 */
	public void commitTransaction() throws SQLException {
		sqlMapClient.commitTransaction();
	}

	/**
	 * 
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#endTransaction()
	 */
	public void endTransaction() throws SQLException {
		int refcnt = getTransactionRefCnt(sqlMapClient);
		setTransactionRefCnt(sqlMapClient, --refcnt);

		if (refcnt == 0) {
			sqlMapClient.endTransaction();
		}
	}

	/**
	 * 
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#getCurrentConnection()
	 */
	public Connection getCurrentConnection() throws SQLException {
		return sqlMapClient.getCurrentConnection();
	}

	public DataSource getDataSource() {
		return sqlMapClient.getDataSource();
	}

	public DataSource getDataSourceSlave() {
		return sqlMapClientSlave.getDataSource();
	}

	/**
	 * 
	 * @return -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#getUserConnection()
	 */
	public Connection getUserConnection() throws SQLException {
		return sqlMapClient.getCurrentConnection();
	}

	public Connection getUserConnectionSlave() throws SQLException {
		return sqlMapClientSlave.getCurrentConnection();
	}

	/**
	 * 
	 * @param arg0 -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#setUserConnection(java.sql.Connection)
	 */
	public void setUserConnection(Connection arg0) throws SQLException {
		sqlMapClient.setUserConnection(arg0);
	}

	public void setUserConnectionSlave(Connection arg0) throws SQLException {
		sqlMapClientSlave.setUserConnection(arg0);
	}

	/**
	 * 
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#startTransaction()
	 */
	public void startTransaction() throws SQLException {
		int refcnt = getTransactionRefCnt(sqlMapClient);

		if (refcnt == 0) {
			sqlMapClient.startTransaction();
		}

		setTransactionRefCnt(sqlMapClient, ++refcnt);
	}

	/**
	 * 
	 * @param arg0 -
	 * @throws SQLException -
	 * @see com.ibatis.sqlmap.client.SqlMapTransactionManager#startTransaction(int)
	 */
	public void startTransaction(int arg0) throws SQLException {
		int refcnt = getTransactionRefCnt(sqlMapClient);

		if (refcnt == 0) {
			sqlMapClient.startTransaction(arg0);
		}

		setTransactionRefCnt(sqlMapClient, ++refcnt);
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClientSlave() {
		return sqlMapClientSlave;
	}

	public void setSqlMapClientSlave(SqlMapClient sqlMapClient) {
		this.sqlMapClientSlave = sqlMapClient;
	}

	public String getSql(String id, Object parameterObject) {
		return SqlMapClientFactory.getSql(sqlMapClient, id, parameterObject);
	}

	public String getSql(String id) {
		return getSql(id, null);
	}

	public boolean isForceMaster() {
		return forceMaster;
	}

	public void setForceMaster(boolean forceMaster) {
		this.forceMaster = forceMaster;
	}
}
