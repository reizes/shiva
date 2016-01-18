package com.reizes.shiva.etl.core.extractor;

import org.apache.log4j.Logger;

public abstract class AbstractExtractor implements Extractor {
	protected Logger log = Logger.getLogger(this.getClass());

	private ExtractedItemHandler extractedItemHandler;

	@Override
	public final void setExtractedItemHandler(ExtractedItemHandler extractedItemHandler) {
		this.extractedItemHandler = extractedItemHandler;
	}

	protected final Object startProcessItem(Object item) throws Exception {
		if (this.extractedItemHandler != null) {
			return this.extractedItemHandler.processExtractedItem(item);
		}
		
		throw new NullItemHandlerException("ExtractedItemHandler is NULL");
	}

	@Deprecated
	public final void extract() throws Exception {
		doProcess(null);
	}

	@Deprecated
	public final void extract(Object parameter) throws Exception {
		doProcess(parameter);
	}

}
