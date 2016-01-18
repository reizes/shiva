package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface AfterItemProcessAware {
	public void onAfterItemProcess(ProcessContext context,Object data) throws Exception;
}
