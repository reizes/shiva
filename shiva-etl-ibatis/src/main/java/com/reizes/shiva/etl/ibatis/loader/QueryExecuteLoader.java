package com.reizes.shiva.etl.ibatis.loader;

import java.lang.reflect.Method;

import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.ibatis.dao.SimpleDao;
import com.reizes.shiva.etl.ibatis.helper.QueryExecuteType;

/**
 * iBatis 쿼리를 실행하는 Loader
 * @author reizes
 * @since 2009.9.24
 */
public class QueryExecuteLoader extends AbstractDaoLoader {
	private String queryId;
	private QueryExecuteType queryType;
	private Method queryMethod;

	public QueryExecuteLoader() {
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public QueryExecuteLoader(String queryId) {
		setQueryId(queryId);
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public QueryExecuteLoader(String queryId, QueryExecuteType queryType) {
		setDao(new SimpleDao());
		setQueryId(queryId);
		try {
			setQueryType(queryType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 지정된 query를 ibatis를 통해 실행한다.
	 * @param input - param
	 * @return - return
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		if (queryMethod == null || queryId == null) {
			throw new InvalidPropertyException("queryMethod or queryId is null");
		}

		return queryMethod.invoke(getDao(), queryId, input);
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public QueryExecuteType getQueryType() {
		return queryType;
	}

	/**
	 * queryType을 세팅하고 적당한 메소드를 찾는다.
	 * @param queryType - QueryExecuteType
	 * @throws SecurityException -
	 * @throws NoSuchMethodException -
	 */
	public void setQueryType(QueryExecuteType queryType) throws SecurityException, NoSuchMethodException {
		this.queryType = queryType;

		if (queryType == QueryExecuteType.DELETE) {
			queryMethod = SimpleDao.class.getMethod("delete", String.class, Object.class);
		} else if (queryType == QueryExecuteType.INSERT) {
			queryMethod = SimpleDao.class.getMethod("insert", String.class, Object.class);
		} else if (queryType == QueryExecuteType.SELECT) {
			queryMethod = SimpleDao.class.getMethod("selectList", String.class, Object.class);
		} else if (queryType == QueryExecuteType.UPDATE) {
			queryMethod = SimpleDao.class.getMethod("update", String.class, Object.class);
		}
	}

}
