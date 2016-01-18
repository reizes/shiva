package com.reizes.shiva.etl.jdbc.extractor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 기존 Extractor를 AbstractResultSetExtractor 상속으로 변경
 * @author reizes
 * @since 2.1.0
 * @since 2010.4.12
 */
public class ResultSetExtractor extends AbstractResultSetExtractor {
	private String query;
	private Statement stmt = null;

	@Override
	protected ResultSet getResultSet(Connection conn) throws SQLException {
		stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		try {
			stmt.setFetchSize(Integer.MIN_VALUE);
		} catch (Exception e) {
			stmt.setFetchSize(1);
		}
		return stmt.executeQuery(query);
	}

	@Override
	protected void beforeCloseResultSet() throws SQLException {
		if (stmt != null) {
			stmt.close();
		}

		stmt = null;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
