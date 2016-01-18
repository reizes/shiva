package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface AfterItemProcessListener {
	public void onAfterItemProcess(ProcessContext context,Object data);
}
