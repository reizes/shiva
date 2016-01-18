package com.reizes.shiva.etl.core.transformer;

import java.util.LinkedList;
import java.util.List;

import com.reizes.shiva.etl.core.ExecutionStatus;
import com.reizes.shiva.etl.core.ProcessLastOneMore;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.context.ProcessContextAware;

/**
 * Object들을 받아 지정 개수까지 List로 묶어서 반환 (List로 반환되지 않으면 SKIP)
 * listSize가 -1이면 모든 아이템을 list로, 0이면 list로 안함, 1 이상이면 주어진 개수만큼 리스트로 만든다.
 * @author reizes
 * @since 2009.11.2
 */
public class ObjectToListTransformer extends AbstractTransformer implements ProcessContextAware, ProcessLastOneMore {
	ProcessContext context;
	private int listSize = 0;
	private int curSize = 0;
	private boolean lastProcess = false;
	List<Object> itemList;

	@Override
	public Object doProcess(Object input) throws Exception {
		if (listSize != 0) {
			if (curSize == 0) {
				itemList = new LinkedList<Object>();
			}
			if (this.lastProcess) {
				return itemList;
			}

			itemList.add(input);
			curSize++;

			if (curSize == listSize) {
				curSize = 0;
				return itemList;
			}

			this.context.setExecutionStatus(ExecutionStatus.SKIP);
			return null;
		}

		return input;
	}

	@Override
	public void setProcessContext(ProcessContext context) {
		this.context = context;
	}

	public int getListSize() {
		return listSize;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	@Override
	public void setLastItem(boolean flag) {
		this.lastProcess = flag;
	}

}
