package com.reizes.shiva.etl.core.mock;

import com.reizes.shiva.etl.core.EtlElement;
import com.reizes.shiva.etl.core.ExecutionStatus;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.context.ProcessContextAware;

public class MockEtlElementRaiseStop implements EtlElement,ProcessContextAware {
	private static int processCount=0;
	private static int stop=0;
	private ProcessContext context;
	
	private static synchronized void increaseProcessCount() {
		MockEtlElementRaiseStop.processCount++;
	}
	
	public static synchronized void resetProcessCount() {
		MockEtlElementRaiseStop.processCount=0;
	}
	
	public static synchronized int getProcessCount() {
		return MockEtlElementRaiseStop.processCount;
	}
	
	@Override
	public Object doProcess(Object input) throws Exception {
		Long data=(Long)input;
		if (data==MockEtlElementRaiseStop.stop) {
			this.context.setExecutionStatus(ExecutionStatus.STOP);
			return data;
		}
		//System.out.println(MockEtlElementRaiseStop.getProcessCount()+" processing");
		MockEtlElementRaiseStop.increaseProcessCount();
		return data;
	}

	@Override
	public void setProcessContext(ProcessContext context) {
		this.context=context;
	}

	public static int getStop() {
		return MockEtlElementRaiseStop.stop;
	}

	public static void setStop(int stop) {
		MockEtlElementRaiseStop.stop = stop;
	}

}
