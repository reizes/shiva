package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface AfterProcessAware {
	public void onAfterProcess(ProcessContext context,Object data) throws Exception;
}
