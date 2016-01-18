/*
 * @(#)LogProcessCountTransformer.java $version 2010. 5. 4.
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.reizes.shiva.etl.core.transformer;

import org.apache.log4j.Priority;

import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.context.ProcessContextAware;

/**
 * 지정 카운트마다 처리 중인 숫자를 기록
 * @author reizes
 * @since 2.0.2
 * @since 2010.5.4
 */
public class LogProcessCountTransformer extends AbstractTransformer implements ProcessContextAware {
	private ProcessContext context;
	private long step = 1000;
	private long initialCount = 0;
	private String msgFormat = "Current Process : %d";
	@SuppressWarnings("deprecation")
	private Priority priority = Priority.INFO;

	/**
	 * @param input
	 * @return
	 * @throws Exception
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		long count = initialCount + this.context.getItemCount();

		if ((count + 1) % step == 0l) {
			log.log(priority, String.format(msgFormat, count + 1));
		}

		return input;
	}

	/**
	 * @param context
	 * @see com.reizes.shiva.etl.core.context.ProcessContextAware#setProcessContext(com.reizes.shiva.etl.core.context.ProcessContext)
	 */
	@Override
	public void setProcessContext(ProcessContext context) {
		this.context = context;
	}

	public long getStep() {
		return step;
	}

	public void setStep(long step) {
		this.step = step;
	}

	public String getMsgFormat() {
		return msgFormat;
	}

	public void setMsgFormat(String msgFormat) {
		this.msgFormat = msgFormat;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public long getInitialCount() {
		return initialCount;
	}

	public void setInitialCount(long initialCount) {
		this.initialCount = initialCount;
	}

}
