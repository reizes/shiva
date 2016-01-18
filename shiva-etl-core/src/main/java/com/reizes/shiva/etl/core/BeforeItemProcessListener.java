package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface BeforeItemProcessListener {
	public void onBeforeItemProcess(ProcessContext context,Object data);
}
