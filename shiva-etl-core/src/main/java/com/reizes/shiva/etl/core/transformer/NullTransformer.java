package com.reizes.shiva.etl.core.transformer;

public class NullTransformer extends AbstractTransformer {

	@Override
	public Object doProcess(Object input) throws Exception {
		return input;
	}
}
