package com.reizes.shiva.etl.ibatis.loader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reizes.shiva.etl.core.AfterProcessAware;
import com.reizes.shiva.etl.core.BeforeProcessAware;
import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.loader.AbstractLoader;
import com.reizes.shiva.etl.ibatis.dao.SimpleDao;
import com.reizes.shiva.etl.ibatis.helper.SqlMapClientFactory;

/**
 * iBatis의 쿼리를 사용할 수 있는 JDBC Batch Loader
 * 단, dynamic SQL을 사용하면 안된다.
 * input은 parameter (Object[])
 * @author reizes
 * @since 2.1.1
 */
public class JdbcSqlMapLoader extends AbstractLoader implements BeforeProcessAware, AfterProcessAware {
	private String query;
	private String queryId;
	private boolean supportsBatchUpdates;
	private int batchUpdateSize = 1000;
	private boolean enableBatchUpdates = true;
	private SimpleDao dao;
	private String dataSourceName;
	private int updatedCount = 0;

	/*
	 * internal properties
	 */
	private Connection connection;
	private PreparedStatement preparedStatement;
	private int curBatchUpdateCnt = 0;

	private void setParameter(Object[] objects, PreparedStatement stmt) throws SQLException {
		int parameterIndex = 1;

		for (Object obj : objects) {
			stmt.setObject(parameterIndex++, obj);
		}
	}

	@Override
	public void onBeforeProcess(ProcessContext context, Object data) throws Exception {
		if (dataSourceName != null) {
			dao = new SimpleDao(SqlMapClientFactory.getSqlMapClient(dataSourceName, true));
		} else {
			throw new InvalidPropertyException("DataSourceName is null");
		}
		if (queryId == null) {
			throw new InvalidPropertyException("query id is null");
		}

		query = dao.getSql(queryId);

		connection = dao.getDataSource().getConnection();

		if (isEnableBatchUpdates()) {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			supportsBatchUpdates = dbMetaData.supportsBatchUpdates();
		}

		try {
			preparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			connection.close();
			connection = null;
			throw e;
		}

		updatedCount = 0;
	}

	@Override
	public void onAfterProcess(ProcessContext context, Object data) throws SQLException {
		if (isSupportsBatchUpdates() && curBatchUpdateCnt > 0) {
			preparedStatement.executeBatch();
			curBatchUpdateCnt = 0;
		}
		if (preparedStatement != null) {
			preparedStatement.close();
		}
		if (connection != null) {
			connection.close();
		}

		preparedStatement = null;
		connection = null;
	}

	/**
	 * loader main
	 * @param input - Object[] query parameter
	 * @return - input
	 * @throws Exception
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		setParameter((Object[])input, preparedStatement);

		if (isSupportsBatchUpdates()) {
			preparedStatement.addBatch();
			curBatchUpdateCnt++;

			if (curBatchUpdateCnt == getBatchUpdateSize()) {
				int[] result = preparedStatement.executeBatch();
				curBatchUpdateCnt = 0;
				
				for (int cnt : result) {
					updatedCount += cnt;
				}
			}
		} else {
			updatedCount += preparedStatement.executeUpdate();
		}

		return input;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public boolean isSupportsBatchUpdates() {
		return supportsBatchUpdates;
	}

	public int getBatchUpdateSize() {
		return batchUpdateSize;
	}

	public void setBatchUpadteSize(int batchUpdateSize) {
		this.batchUpdateSize = batchUpdateSize;
	}

	public boolean isEnableBatchUpdates() {
		return enableBatchUpdates;
	}

	public void setEnableBatchUpdates(boolean enableBatchUpdates) {
		this.enableBatchUpdates = enableBatchUpdates;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public int getUpdatedCount() {
		return updatedCount;
	}

}
