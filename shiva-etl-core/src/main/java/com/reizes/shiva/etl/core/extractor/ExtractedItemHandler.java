package com.reizes.shiva.etl.core.extractor;

public interface ExtractedItemHandler {
	public Object processExtractedItem(Object item) throws Exception;
}
