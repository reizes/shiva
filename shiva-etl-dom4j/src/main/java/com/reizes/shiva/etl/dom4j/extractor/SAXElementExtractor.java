package com.reizes.shiva.etl.dom4j.extractor;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import com.reizes.shiva.etl.core.EtlInterruptException;
import com.reizes.shiva.etl.core.ExecutionStatus;
import com.reizes.shiva.etl.core.ProcessStatus;
import com.reizes.shiva.etl.core.context.ProcessContext;
import com.reizes.shiva.etl.core.context.ProcessContextAware;
import com.reizes.shiva.etl.core.extractor.AbstractExtractor;

/**
 * dom4j의 SAXReader를 이용하여 XML을 파싱하는 Extractor
 * @author reizes
 * @since 2009.9.17
 */
public class SAXElementExtractor extends AbstractExtractor implements ElementHandler, ProcessContextAware {
	private ProcessContext context;
	private String handlePath; // Extract 기준 태그
	private String encoding;
	private int limit = -1;
	private int curProcess = 0;
	private SAXReader saxReader;

	private boolean validation;
	private Map<String, Boolean> features = new HashMap<String, Boolean>();

	/**
	 * validation 설정
	 * @param validation true/false
	 */
	public void setValidation(boolean validation) {
		this.validation = validation;
	}

	/**
	 * SAX 파서의 feature를 설정한다.
	 * @param name feature 이름
	 * @param value true/false
	 */
	public void setFeature(String name, boolean value) {
		features.put(name, value);
	}

	/**
	 * onEnd Handler
	 * @param path - ElementPath
	 * @see org.dom4j.ElementHandler#onEnd(org.dom4j.ElementPath)
	 */
	@Override
	public void onEnd(ElementPath path) {
		Element element = path.getCurrent();
		context.put("lastProcessedElement", element);

		try {
			context.put("element_index", curProcess);
			startProcessItem(element);
			element.detach();
			curProcess++;

			if (this.limit > 0 && curProcess >= this.limit) {
				throw new EtlInterruptException("item process limit exceeded");
			}
		} catch (EtlInterruptException e) {
			saxReader.resetHandlers();
			context.setExecutionStatus(ExecutionStatus.STOP);
			context.setProcessStatus(ProcessStatus.FINISHED);
			throw e;
		} catch (Exception e) {
			context.setExecutionStatus(ExecutionStatus.STOP);
			context.setProcessStatus(ProcessStatus.FAILED);
			context.getLogger().error("SAXExtractor.onEnd failed", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * @param arg0 -
	 * @see org.dom4j.ElementHandler#onStart(org.dom4j.ElementPath)
	 */
	@Override
	public void onStart(ElementPath arg0) {
	}

	@Override
	public void setProcessContext(ProcessContext context) {
		this.context = context;
	}

	public ProcessContext getProcessContext() {
		return context;
	}

	/**
	 * SAXReader 메인 프로세스
	 * @param input - InputStream
	 * @return - null
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@Override
	public Object doProcess(Object input) throws Exception {
		InputStream is = (InputStream)input;
		saxReader = new SAXReader();
		saxReader.addHandler(handlePath, this);
		saxReader.setValidation(validation);

		if (encoding != null) {
			saxReader.setEncoding(encoding);
		}

		if (!features.isEmpty()) {
			for (Entry<String, Boolean> entry : features.entrySet()) {
				saxReader.setFeature(entry.getKey(), entry.getValue());
			}
		}

		try {
			saxReader.read(is);
		} finally {
			if (is != null) {
				is.close();
			}
		}

		return input;
	}

	public String getHandlePath() {
		return handlePath;
	}

	public void setHandlePath(String handlePath) {
		this.handlePath = handlePath;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
