package com.xinpinv.service.impl;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.xinpinv.service.BaseDao;

/**
 * ����DAOʵ����
 * @time 2014��7��23�� 20:18:15
 * @author CHENYR
 *
 */
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

	/**
     * �������
     */
	@Override
    public void save(Object obj) {
        getHibernateTemplate().save(obj);
    }
}
