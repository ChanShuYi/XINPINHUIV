package com.xinpinv.test;

/**
 * POJOע�⵼�����ݿ������
 * ���ڲ���Annotationע���POJO���Ƿ����Զ��������ݿ��
 * ʱ�䣺2014��6��4�� 10:15:06
 * @author chenyr
 */

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;


public class HibernateUtil {
    
    @Test public void initiateHibernate()
    {
        /* ���������AnnotatioinConfiguration��,��Ϊ������ע�����õ�,�������������ļ����õ� */
        AnnotationConfiguration config = new AnnotationConfiguration().configure();
        
         SchemaExport export = new SchemaExport(config);  
         export.create(true, true);  
         System.out.println("End");
    }
}