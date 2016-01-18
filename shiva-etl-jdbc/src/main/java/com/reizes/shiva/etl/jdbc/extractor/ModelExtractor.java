package com.reizes.shiva.etl.jdbc.extractor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;

import com.reizes.shiva.etl.core.InvalidPropertyException;
import com.reizes.shiva.etl.jdbc.helper.TypeHandler;
import com.reizes.shiva.utils.StringUtil;

/*
 * Extract ResultSet To Map
 */
public class ModelExtractor extends ResultSetExtractor {
	private static HashMap<Class<?>, TypeHandler> typeHandlerMap;
	private Class<?> modelClass;
	private boolean useCamel = true;

	@Override
	protected Object fetchResultSet(ResultSet rs, Object input) throws Exception {
		if (rs == null) {
			throw new InvalidPropertyException("ResultSet is null");
		}
		if (modelClass == null) {
			throw new InvalidPropertyException("ModelClass is null");
		}

		ResultSetMetaData metaData = rs.getMetaData();
		int colCnt = metaData.getColumnCount();

		while (rs.next()) {
			Object model = modelClass.newInstance();

			for (int index = 1; index <= colCnt; index++) {
				String colName = metaData.getColumnLabel(index).toLowerCase(); // 컬럼 이름은 소문자로 간주

				if (isUseCamel()) {
					colName = StringUtil.camelize(colName);
				}
				if (PropertyUtils.isWriteable(model, colName)) {
					try {
						Object data = rs.getObject(index);
						Class<?> targetType = PropertyUtils.getPropertyType(model, colName);

						if (typeHandlerMap != null) {
							TypeHandler handler = typeHandlerMap.get(targetType);

							if (handler != null) {
								data = handler.dbToUserType(data);
							}
						}

						Object converted = ConvertUtils.convert(data, PropertyUtils.getPropertyType(model, colName));
						PropertyUtils.setSimpleProperty(model, colName, converted);
					} catch (SQLException e) {
						log.error("SQLException occurred! : " + colName + " is NULL");
					}
				}
			}

			input = startProcessItem(modelClass.cast(model));
			executeInvalidateQuery();
		}

		return input;
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

	public static void addTypeHandler(Class<?> type, TypeHandler handler) {
		if (typeHandlerMap == null) {
			typeHandlerMap = new HashMap<Class<?>, TypeHandler>();
		}

		typeHandlerMap.put(type, handler);
	}
}
