package com.reizes.shiva.etl.ibatis.loader;

import java.util.LinkedList;
import java.util.List;

import com.reizes.shiva.etl.core.AfterProcessAware;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.ibatis.helper.QueryExecuteType;

/**
 * iBatis의 배치 기능을 이용할 수 있는 Insert Querty Loader
 * @author reizes
 * @since 2009.9.24
 */
public class BatchInsertLoader extends QueryExecuteLoader implements AfterProcessAware {
	private int bufferSize = 0;
	private int curcnt = 0;
	private List<Object> objectList;

	public BatchInsertLoader() {
		super();
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		objectList = new LinkedList<Object>();
	}

	public BatchInsertLoader(String queryId) {
		super(queryId);
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		objectList = new LinkedList<Object>();
	}

	public BatchInsertLoader(int bufferSize) {
		super();
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		objectList = new LinkedList<Object>();
		setBufferSize(bufferSize);
	}

	public BatchInsertLoader(String queryId, int bufferSize) {
		super(queryId);
		try {
			setQueryType(QueryExecuteType.INSERT);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		objectList = new LinkedList<Object>();
		setBufferSize(bufferSize);
	}

	/**
	 * 배치 insert를 수행하는 프로세스
	 * @param input - 입력 데이터
	 * @return - 출력 데이터
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.ibatis.loader.QueryExecuteLoader#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		if (bufferSize == 0) {
			getDao().insert(getQueryId(), input);
		} else {
			objectList.add(input);

			if (curcnt >= bufferSize - 1) {
				getDao().insertBatch(getQueryId(), objectList);
				objectList.clear();
				curcnt = 0;
			} else {
				curcnt++;
			}
		}

		return input;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	/**
	 * 프로세스 수행 후 남은 데이터가 있으면 마저 처리
	 * @param context -
	 * @param data -
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.AfterProcessAware#onAfterProcess(com.reizes.shiva.etl.core.context.ProcessContext, java.lang.Object)
	 */
	@Override
	public void onAfterProcess(ProcessContext context, Object data) throws Exception {
		if (bufferSize > 0) {
			getDao().insertBatch(getQueryId(), objectList);
			objectList.clear();
		}
	}

}
