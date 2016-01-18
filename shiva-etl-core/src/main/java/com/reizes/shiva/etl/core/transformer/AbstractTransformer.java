package com.reizes.shiva.etl.core.transformer;

import org.apache.log4j.Logger;

public abstract class AbstractTransformer implements Transformer {
	protected Logger log = Logger.getLogger(this.getClass());
}
