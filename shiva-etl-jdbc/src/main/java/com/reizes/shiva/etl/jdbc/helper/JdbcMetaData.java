package com.reizes.shiva.etl.jdbc.helper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcMetaData {
	public void setFromResultSet(ResultSet rs) throws SQLException;
}
