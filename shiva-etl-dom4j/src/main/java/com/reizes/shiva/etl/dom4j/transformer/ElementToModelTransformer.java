package com.reizes.shiva.etl.dom4j.transformer;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Element;

import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.core.transformer.AbstractTransformer;
import com.reizes.shiva.utils.StringUtil;

/**
 * dom4j의 Element를 지정된 Model로 변환하는 Transformer
 * @author reizes
 * @since 2009.9.17
 */
public class ElementToModelTransformer extends AbstractTransformer {
	private Class<?> modelClass;
	private boolean useCamel = true;

	/**
	 * dom4j의 Element를 지정된 Model로 변환하는 Transformer
	 * @param input - Element
	 * @return - Model
	 * @throws Exception -
	 * @see com.reizes.shiva.etl.core.EtlElement#doProcess(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object doProcess(Object input) throws Exception {
		if (modelClass == null) {
			throw new InvalidPropertyException("modelClass is null!");
		}

		Element element = (Element)input;

		Object model = this.modelClass.newInstance();

		List<Element> list = (List<Element>)element.elements();

		for (Element el : list) {
			String name = el.getName();
			String value = el.getTextTrim();
			el.detach();

			if (useCamel) {
				name = StringUtil.camelize(name);
			}
			try {
				if (PropertyUtils.isWriteable(model, name)) {
					PropertyUtils.setSimpleProperty(model, name, ConvertUtils.convert(value, PropertyUtils.getPropertyType(model, name)));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}

		list.clear();

		return model;
	}

	public Class<?> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<?> modelClass) {
		this.modelClass = modelClass;
	}

	public boolean isUseCamel() {
		return useCamel;
	}

	public void setUseCamel(boolean useCamel) {
		this.useCamel = useCamel;
	}

}
