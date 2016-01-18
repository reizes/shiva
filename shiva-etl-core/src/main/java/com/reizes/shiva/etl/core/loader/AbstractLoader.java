package com.reizes.shiva.etl.core.loader;

import org.apache.log4j.Logger;

public abstract class AbstractLoader implements Loader {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Deprecated
	public final void prepareLoad() throws Exception {
		
	}
	@Deprecated
	public final void load(Object data) throws Exception {
		doProcess(data);
	}
	@Deprecated
	public final void delete(Object parameter) throws Exception {
		
	}
	@Deprecated
	public final void finishLoad() throws Exception {
		
	}
}
