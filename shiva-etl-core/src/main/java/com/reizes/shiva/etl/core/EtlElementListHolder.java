package com.reizes.shiva.etl.core;

import java.util.List;

public interface EtlElementListHolder {
	public List<EtlElement> getElementList();
	public void setElementList(List<EtlElement> elementList);
	public void setElement(EtlElement element);
	public void addElement(EtlElement element);
}
