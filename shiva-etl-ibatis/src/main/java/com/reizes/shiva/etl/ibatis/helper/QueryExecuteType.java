package com.reizes.shiva.etl.ibatis.helper;

/**
 * Query 실행 타입
 * @author reizes
 * @since 2009.9.24
 */
public enum QueryExecuteType {
	SELECT(0), INSERT(1), UPDATE(2), DELETE(3);

	private final int type;

	QueryExecuteType(int type) {
		this.type = type;
	}
	
	/**
	 * int값 반환
	 * @return - int
	 */
	public int intValue() {
		return type;
	}
}
