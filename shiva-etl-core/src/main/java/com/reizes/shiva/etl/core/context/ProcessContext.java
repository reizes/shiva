package com.reizes.shiva.etl.core.context;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.reizes.shiva.etl.core.EtlProcessor;
import com.reizes.shiva.etl.core.ExecutionStatus;
import com.reizes.shiva.etl.core.ProcessStatus;

public class ProcessContext {
	private EtlProcessor processor;
	private Object processParameter; // 2.1.0 ETL Process의 doProcess로 들어온 파라메터 저장
	private Map<String, Object> map;
	private ExecutionStatus executionStatus;
	private ProcessStatus processStatus;
	private boolean throwException = true;
	private long itemCount = 0;
	private long skipCount = 0;	// 2.1.1-SNAPSHOT 
	private Logger logger = Logger.getLogger(this.getClass());

	public ProcessContext(EtlProcessor processor) {
		this.processor = processor;
		this.map = new HashMap<String, Object>();
		initContext();
	}

	public void initContext() {
		this.executionStatus = ExecutionStatus.CONTINUE;
		this.processStatus = ProcessStatus.UNKNOWN;
	}

	public EtlProcessor getProcessor() {
		return processor;
	}

	public void put(String name, Object value) {
		this.map.put(name, value);
	}

	public Object get(String name) {
		return this.map.get(name);
	}

	public ExecutionStatus getExecutionStatus() {
		return executionStatus;
	}

	public void setExecutionStatus(ExecutionStatus executionStatus) {
		this.executionStatus = executionStatus;
	}

	public ProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(ProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public boolean isThrowException() {
		return throwException;
	}

	public void setThrowException(boolean throwException) {
		this.throwException = throwException;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public long getItemCount() {
		return itemCount;
	}

	public void setItemCount(long itemCount) {
		this.itemCount = itemCount;
	}

	public Map<String, Object> getContextDataMap() {
		return map;
	}

	public void setContextDataMap(Map<String, Object> map) {
		this.map = map;
	}

	public Object getProcessParameter() {
		return processParameter;
	}

	public void setProcessParameter(Object processParameter) {
		this.processParameter = processParameter;
	}

	public long getSkipCount() {
		return skipCount;
	}

	public void setSkipCount(long skipCount) {
		this.skipCount = skipCount;
	}
}
