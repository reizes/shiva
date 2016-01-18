package com.reizes.shiva.etl.core.extractor;

import com.reizes.shiva.etl.core.EtlElement;

public interface Extractor extends EtlElement {
	public void setExtractedItemHandler(ExtractedItemHandler extractedItemHandler);
}
