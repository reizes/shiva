package com.reizes.shiva.etl.ibatis.extractor;

import java.sql.SQLException;
import java.sql.Statement;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.event.RowHandler;

import com.reizes.shiva.etl.core.BeforeProcessAware;
import com.reizes.shiva.etl.core.EtlException;
import com.reizes.shiva.etl.core.ExecutionStatus;
import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.ProcessStatus;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.context.ProcessContextAware;
import com.reizes.shiva.etl.core.extractor.AbstractExtractor;
import com.reizes.shiva.etl.ibatis.helper.SqlMapClientFactory;

/**
 * iBatis의 SQL-Map을 이용하는 Extractor
 * @author reizes
 * @since 2009.9.18
 * @since 2010.10.18
 */
public class IbatisExtractor extends AbstractExtractor implements BeforeProcessAware, RowHandler, ProcessContextAware {
	private String sqlMapConfigPath;
	private String dataSourcePropertyPath;
	private String selectQueryId;

	// internal user
	private SqlMapClient sqlMapClient;
	private String dataSourceName; // 2.1.0
	private ProcessContext context;

	// 2012.8.7 invalidate query - 내부적으로 지정 카운트당 invalidate query를 수행한다.
	private String invalidateQuery = "SELECT 1";
	private long invalidateCount = 5000; // 5000개 마다 invalidate
	private long currentCount = 0;

	/**
	 * iBatis의 queryWithRowHandler를 주어진 파라메터를 가지고 호출한다.
	 * @param input - parameter Object
	 * @return - input
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		sqlMapClient.queryWithRowHandler(selectQueryId, input, this);
		return input;
	}

	public void setSelectQueryId(String selectQueryId) {
		this.selectQueryId = selectQueryId;
	}

	/**
	 * ETL에 의해 호출되며, 프로세스 시작 전 SqlMap을 초기화 한다.
	 * @since 2.1.0 - dataSourceName이 지정된 경우 adhoc connection으로 한다.
	 * @param context -
	 * @param data -
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.BeforeProcessAware#onBeforeProcess(com.reizes.shiva.etl.core.context.ProcessContext, java.lang.Object)
	 */
	@Override
	public void onBeforeProcess(ProcessContext context, Object data) throws Exception {
		currentCount = 0;

		if (dataSourceName != null) {
			sqlMapClient = SqlMapClientFactory.getSqlMapClient(dataSourceName, true);
		} else {
			if (sqlMapConfigPath == null) {
				throw new InvalidPropertyException("sqlMapConfigPath is null");
			}
			if (selectQueryId == null) {
				throw new InvalidPropertyException("selectQueryId is null");
			}

			sqlMapClient = SqlMapClientFactory.getAdhocSqlMapClient(sqlMapConfigPath, dataSourcePropertyPath);
		}
	}

	/**
	 * 2012.8.7 invalidate query를 실행한다.
	 * @throws SQLException -
	 */
	private void executeInvalidateQuery() throws SQLException {
		Statement stmt = sqlMapClient.getCurrentConnection().createStatement();
		stmt.execute(invalidateQuery);
		stmt.close();
	}

	/**
	 * iBatis에 의해 호출되는 RowHandler
	 * @param arg0 -
	 * @see com.ibatis.sqlmap.client.event.RowHandler#handleRow(java.lang.Object)
	 */
	@Override
	public void handleRow(Object arg0) {
		try {
			startProcessItem(arg0);
			currentCount++;

			if (currentCount >= invalidateCount) {
				currentCount = 0;
				//executeInvalidateQuery();
			}
		} catch (Exception e) {
			context.setExecutionStatus(ExecutionStatus.STOP);
			context.setProcessStatus(ProcessStatus.FAILED);
			context.getLogger().error("IbatisExtractor.handleRow failed", e);
			throw new EtlException(e);
		}
	}

	@Override
	public void setProcessContext(ProcessContext context) {
		this.context = context;
	}

	public String getDataSourcePropertyPath() {
		return dataSourcePropertyPath;
	}

	public void setDataSourcePropertyPath(String dataSourcePropertyPath) {
		this.dataSourcePropertyPath = dataSourcePropertyPath;
	}

	public String getSqlMapConfigPath() {
		return sqlMapConfigPath;
	}

	public void setSqlMapConfigPath(String sqlMapConfigPath) {
		this.sqlMapConfigPath = sqlMapConfigPath;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public long getInvalidateCount() {
		return invalidateCount;
	}

	public void setInvalidateCount(long invalidateCount) {
		this.invalidateCount = invalidateCount;
	}

	public String getInvalidateQuery() {
		return invalidateQuery;
	}

	public void setInvalidateQuery(String invalidateQuery) {
		this.invalidateQuery = invalidateQuery;
	}

}
