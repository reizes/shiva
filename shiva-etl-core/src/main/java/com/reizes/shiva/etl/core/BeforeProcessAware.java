package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface BeforeProcessAware {
	public void onBeforeProcess(ProcessContext context,Object data) throws Exception;
}
