package com.reizes.shiva.etl.jdbc.loader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import com.reizes.shiva.etl.core.AfterProcessAware;
import com.reizes.shiva.etl.core.BeforeProcessAware;
import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.loader.AbstractLoader;

public abstract class AbstractJDBCLoader extends AbstractLoader implements BeforeProcessAware, AfterProcessAware {
	private DataSource datasource;
	private String query;
	private boolean supportsBatchUpdates;
	private int batchUpdateSize = 1000;
	private boolean enableBatchUpdates = true;

	/*
	 * internal properties
	 */
	private Connection connection;
	private Pattern valuePattern = Pattern.compile("#([^\\#]+)#", Pattern.MULTILINE);
	private Pattern replacePattern = Pattern.compile("\\$([^\\$]+)\\$", Pattern.MULTILINE);
	private LinkedList<String> parameterList;
	private String processedQuery;
	private PreparedStatement preparedStatement;
	private boolean doReplace = false; // $column$가 포함되었는지 여부
	private int curBatchUpdateCnt = 0;

	abstract protected Object getData(Object object, String name) throws Exception;

	/*
	 * prepare parameter value : #column# -> ?
	 */
	private void prepareQuery() {
		parameterList = new LinkedList<String>();
		Matcher matcher = valuePattern.matcher(query);
		
		while (matcher.find()) {
			parameterList.add(matcher.group(1));
		}
		
		processedQuery = matcher.replaceAll("?");

		matcher.reset(processedQuery);
		matcher.usePattern(replacePattern);
		doReplace = matcher.find();
	}

	/*
	 * process query replace : $column$ -> value
	 */
	private String processQueryReplace(Object object) throws Exception {
		StringBuilder sb = new StringBuilder(processedQuery);
		Matcher matcher = replacePattern.matcher(processedQuery);
		int offset = 0;
		
		while (matcher.find()) {
			String name = matcher.group(1);
			String value = getData(object, name).toString();
			sb.replace(matcher.start() + offset, matcher.end() + offset, value);
			offset += value.length() - (name.length() + 2);
		}
		
		return sb.toString();
	}

	private void setParameter(Object object, PreparedStatement stmt) throws Exception {
		int parameterIndex = 1;
		
		for (String name : parameterList) {
			stmt.setObject(parameterIndex, getData(object, name));
			parameterIndex++;
		}
	}

	@Override
	public void onBeforeProcess(ProcessContext context, Object data) throws Exception {
		if (query == null) {
			throw new InvalidPropertyException("query is null");
		}
		if (datasource == null) {
			throw new InvalidPropertyException("datasource is null");
		}
		
		prepareQuery();
		connection = datasource.getConnection();
		
		if (isEnableBatchUpdates()) {
			DatabaseMetaData dbMetaData = connection.getMetaData();
			supportsBatchUpdates = dbMetaData.supportsBatchUpdates();
		}
		if (!doReplace) {
			try {
				preparedStatement = connection.prepareStatement(processedQuery);
			} catch (SQLException e) {
				connection.close();
				connection = null;
				throw e;
			}
		}
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

	@Override
	public Object doProcess(Object input) throws Exception {
		if (doReplace) {
			preparedStatement = connection.prepareStatement(processQueryReplace(input));
			preparedStatement.clearParameters();
			setParameter(input, preparedStatement);
			preparedStatement.executeUpdate();
			preparedStatement.close();
		} else {
			setParameter(input, preparedStatement);
			
			if (isSupportsBatchUpdates()) {
				preparedStatement.addBatch();
				curBatchUpdateCnt++;
				
				if (curBatchUpdateCnt == getBatchUpdateSize()) {
					preparedStatement.executeBatch();
					curBatchUpdateCnt = 0;
				}
			} else {
				preparedStatement.executeUpdate();
			}
		}
		
		return input;
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
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

}
