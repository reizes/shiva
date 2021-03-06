package com.reizes.shiva.etl.core.mock;

import com.reizes.shiva.etl.core.EtlElement;

public class MockEtlElement implements EtlElement {
	private static int processCount=0;
	
	private static synchronized void increaseProcessCount() {
		MockEtlElement.processCount++;
	}
	
	public static synchronized void resetProcessCount() {
		MockEtlElement.processCount=0;
	}
	
	public static synchronized int getProcessCount() {
		return MockEtlElement.processCount;
	}
	
	@Override
	public Object doProcess(Object input) throws Exception {
		//System.out.println(MockEtlElement.getProcessCount()+" processing");
		MockEtlElement.increaseProcessCount();
		return input;
	}

}
