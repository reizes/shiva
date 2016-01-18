package com.reizes.shiva.etl.ibatis.dao;

import java.sql.SQLException;
import java.util.List;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapExecutor;

import com.reizes.shiva.etl.ibatis.helper.SqlMapClientFactory;

/**
 * 기본 CRUD 메소드를 지원하는 Simple DAO
 * @author reizes
 * @since 2009.9.24
 */
public class SimpleDao extends BaseDao {
	public SimpleDao() {
		super();
	}

	public SimpleDao(SqlMapClient sqlMapClient) {
		super(sqlMapClient);
	}

	public SimpleDao(SqlMapClient sqlMapClient, SqlMapClient sqlMapClientSlave) {
		super(sqlMapClient, sqlMapClientSlave);
	}

	public SimpleDao(String sqlMapClientKey) {
		super(SqlMapClientFactory.getSqlMapClient(sqlMapClientKey));
	}

	public SimpleDao(String sqlMapClientKey, String sqlMapClientSlaveKey) {
		super(SqlMapClientFactory.getSqlMapClient(sqlMapClientKey), SqlMapClientFactory.getSqlMapClient(sqlMapClientSlaveKey));
	}

	/**
	 * delete 쿼리 실행
	 * @param queryId - sql map query id
	 * @param param - Parameter
	 * @return - int
	 * @throws SQLException -
	 * @see com.reizes.shiva.etl.ibatis.dao.BaseDao#delete(java.lang.String, java.lang.Object)
	 */
	@Override
	public int delete(String queryId, Object param) throws SQLException {
		return super.delete(queryId, param);
	}

	/**
	 * insert 쿼리 실행
	 * @param queryId - sql map query id
	 * @param param - Parameter
	 * @return - object
	 * @throws SQLException -
	 * @see com.reizes.shiva.etl.ibatis.dao.BaseDao#insert(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object insert(String queryId, Object param) throws SQLException {
		return super.insert(queryId, param);
	}

	/**
	 * update 쿼리 실행
	 * @param queryId - sql map query id
	 * @param param - Parameter
	 * @return - object
	 * @throws SQLException -
	 * @see com.reizes.shiva.etl.ibatis.dao.BaseDao#update(java.lang.String, java.lang.Object)
	 */
	@Override
	public int update(String queryId, Object param) throws SQLException {
		return super.update(queryId, param);
	}

	/**
	 * select 쿼리 실행 Object List로 반환
	 * @param queryId - sql map query id
	 * @param param - Parameter
	 * @return - object
	 * @throws SQLException -
	 */
	public List<?> selectList(String queryId, Object param) throws SQLException {
		return super.queryForList(queryId, param);
	}

	/**
	 * select 쿼리 실행 단일 Object 반환
	 * @param queryId - sql map query id
	 * @param param - Parameter
	 * @return - object
	 * @throws SQLException -
	 */
	public Object selectObject(String queryId, Object param) throws SQLException {
		return super.queryForObject(queryId, param);
	}

	/**
	 * 파라메터 리스트를 받아 배치로 업데이트
	 * @param statementName - 쿼리 ID 
	 * @param parameters - 파라메터 List
	 * @throws SQLException - 
	 */
	public void updateBatch(String statementName, List<?> parameters) throws SQLException {
		SqlMapExecutor executor = getSqlMapClient();
		executor.startBatch();

		for (Object param : parameters) {
			executor.update(statementName, param);
		}

		executor.executeBatch();
	}

	/**
	 * 파라메터 리스트를 받아 배치로 insert
	 * @param statementName - 쿼리 ID
	 * @param parameters - 파라메터 List
	 * @throws SQLException -
	 */
	public void insertBatch(String statementName, List<?> parameters) throws SQLException {
		SqlMapExecutor executor = getSqlMapClient();
		executor.startBatch();

		for (Object param : parameters) {
			executor.insert(statementName, param);
		}

		executor.executeBatch();
	}
}
