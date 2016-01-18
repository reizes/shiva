package com.reizes.shiva.etl.core;

import com.reizes.shiva.etl.core.context.ProcessContext;

public interface ExceptionListener {
	public void onException(ProcessContext context,Object input,Exception e);
}
