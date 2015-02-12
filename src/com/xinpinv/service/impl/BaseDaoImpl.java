package com.xinpinv.service.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.xinpinv.service.BaseDao;

/**
 * 公共DAO实现类
 * @time 2014年7月23日 20:18:15
 * @author CHENYR
 *
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

	/**
     * 保存对象
     */
	@Override
    public void save(Object obj) {
        getHibernateTemplate().save(obj);
    }
}
