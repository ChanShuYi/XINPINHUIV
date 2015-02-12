package com.xinpinv.test;

/**
 * POJO注解导入数据库测试类
 * 用于测试Annotation注解的POJO类是否能自动生成数据库表
 * 时间：2014年6月4日 10:15:06
 * @author chenyr
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;


public class HibernateUtil {
    
    @Test public void initiateHibernate()
    {
        /* 这里必须用AnnotatioinConfiguration类,因为你是用注解配置的,而不是用配置文件配置的 */
        AnnotationConfiguration config = new AnnotationConfiguration().configure();
        
         SchemaExport export = new SchemaExport(config);  
         export.create(true, true);  
         System.out.println("End");
    }
}