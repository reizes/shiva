package com.reizes.shiva.etl.jdbc.dao;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * java.util.Date 타입 핸들러 (java.util.Date, java.sql.Date, java.sql.Time, java.sql.Timestamp 모두 적용)
 * @author inho
 * @since 2009-12-10
 */
public class DateTypeHandler implements TypeHandler<Date, Date> {

	public Date getDBTypeValue(Object value) throws Exception {
		return (Date)value;
	}

	public Date getJavaTypeValue(Object value) throws Exception {
		if (value instanceof Date) {
			return (Date)value;
		}

		if (value instanceof java.sql.Date) {
			return (java.sql.Date)value;
		}

		if (value instanceof Time) {
			return (Time)value;
		}

		if (value instanceof Timestamp) {
			return (Timestamp)value;
		}

		return null;
	}

}
