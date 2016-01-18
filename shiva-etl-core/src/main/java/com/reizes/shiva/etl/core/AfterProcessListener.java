package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface AfterProcessListener {
	public void onAfterProcess(ProcessContext context,Object output);
}
