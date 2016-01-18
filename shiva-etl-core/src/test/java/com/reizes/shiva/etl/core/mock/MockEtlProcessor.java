package com.reizes.shiva.etl.core.mock;

import com.reizes.shiva.etl.core.EtlElementList;
import com.reizes.shiva.etl.core.extractor.ExtractedItemHandler;

public class MockEtlProcessor extends EtlElementList implements
		ExtractedItemHandler {

	@Override
	public Object processExtractedItem(Object item) throws Exception {
		return doProcess(item);
	}

}
