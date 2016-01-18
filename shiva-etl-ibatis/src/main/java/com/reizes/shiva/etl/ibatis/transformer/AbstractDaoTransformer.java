package com.reizes.shiva.etl.ibatis.transformer;

import com.reizes.shiva.etl.core.transformer.AbstractTransformer;
import com.reizes.shiva.etl.ibatis.dao.SimpleDao;

/**
 * iBatis SimpleDao를 사용할 수 있는 abstract transformer
 * @author reizes
 * @since 2009.10.01
 */
public abstract class AbstractDaoTransformer extends AbstractTransformer {
	SimpleDao dao;
	
	public AbstractDaoTransformer() {
		dao = new SimpleDao();
	}

	public SimpleDao getDao() {
		return dao;
	}

	public void setDao(SimpleDao dao) {
		this.dao = dao;
	}
	
}
