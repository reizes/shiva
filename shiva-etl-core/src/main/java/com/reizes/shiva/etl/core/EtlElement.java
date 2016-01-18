package com.reizes.shiva.etl.core;

public interface EtlElement {
	public Object doProcess(Object input) throws Exception;
}
