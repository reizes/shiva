package com.reizes.shiva.etl.ibatis.loader;

import com.reizes.shiva.etl.core.loader.AbstractLoader;
import com.reizes.shiva.etl.ibatis.dao.SimpleDao;

/**
 * iBatis SimpleDao를 사용할 수 있는 abstract loader
 * @author reizes
 * @since 2009.10.07
 */
public abstract class AbstractDaoLoader extends AbstractLoader {
	SimpleDao dao;
	
	public AbstractDaoLoader() {
		dao = new SimpleDao();
	}

	public SimpleDao getDao() {
		return dao;
	}

	public void setDao(SimpleDao dao) {
		this.dao = dao;
	}

}
