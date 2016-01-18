package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface BeforeProcessListener {
	public void onBeforeProcess(ProcessContext context,Object input);
}
