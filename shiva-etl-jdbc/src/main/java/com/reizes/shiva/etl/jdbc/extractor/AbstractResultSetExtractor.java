package com.reizes.shiva.etl.jdbc.extractor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.extractor.AbstractExtractor;

/**
 * ResultSetExractor 상위 클래스 생성
 * @author reizes
 * @since 2.1.0
 * @since 2010.4.12
 */
public abstract class AbstractResultSetExtractor extends AbstractExtractor {
	private DataSource datasource;
	private Connection conn = null;

	// 2012.8.7 invalidate query - 내부적으로 지정 카운트당 invalidate query를 수행한다.
	private String invalidateQuery = "SELECT 1";
	private long invalidateCount = 5000; // 5000개 마다 invalidate
	private long currentCount = 0;

	protected abstract ResultSet getResultSet(Connection conn) throws Exception;

	protected abstract void beforeCloseResultSet() throws Exception;

	@Override
	public Object doProcess(Object input) throws Exception {
		ResultSet rs = null;
		Object output = input;
		Exception ex = null;
		currentCount = 0;

		if (datasource == null) {
			throw new InvalidPropertyException("datasource is null");
		}
		try {
			conn = datasource.getConnection();
			rs = getResultSet(conn);
			rs.setFetchSize(1);
			output = fetchResultSet(rs, output);
		} catch (Exception e) {
			ex = e;
		} finally {
			beforeCloseResultSet();

			if (rs != null) {
				rs.close();
			}
			if (conn != null) {
				conn.close();
			}

			rs = null;
			conn = null;
		}
		if (ex != null) {
			throw ex;
		}

		return output;
	}

	protected Object fetchResultSet(ResultSet rs, Object input) throws Exception {
		while (rs.next()) {
			input = startProcessItem(rs);
			executeInvalidateQuery();
		}
		return input;
	}

	/**
	 * 2012.8.7 invalidate query를 실행한다. - fetchResultSet 내에서 startProcessItem 호출 후 호출되어야 한다.
	 * @throws SQLException -
	 */
	protected void executeInvalidateQuery() throws SQLException {
		if (conn != null) {
			currentCount++;

			if (currentCount >= invalidateCount) {
				currentCount = 0;
				Statement stmt = conn.createStatement();
				stmt.execute(invalidateQuery);
				stmt.close();
			}
		}
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	public String getInvalidateQuery() {
		return invalidateQuery;
	}

	public void setInvalidateQuery(String invalidateQuery) {
		this.invalidateQuery = invalidateQuery;
	}

	public long getInvalidateCount() {
		return invalidateCount;
	}

	public void setInvalidateCount(long invalidateCount) {
		this.invalidateCount = invalidateCount;
	}

}
