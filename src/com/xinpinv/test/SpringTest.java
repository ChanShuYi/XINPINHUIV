package com.xinpinv.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinpinv.pojo.BitInfo;
import com.xinpinv.pojo.Product;
import com.xinpinv.service.BaseDao;

public class SpringTest {
    
    static ApplicationContext ctx = null;
    @Test public void initParam() throws ParseException
    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        BaseDao dao = (BaseDao)ctx.getBean("baseDao");
        
        BitInfo info = new BitInfo();
        info.setUsername("cyr");
        info.setPrice((float)12.22);
        info.setBitTime(new SimpleDateFormat("yyyy-MM-dd").parse("2014-07-25"));
        info.setIp("12.23.25.65");
        info.setAddress("广东省深圳市");
        
        Product product = new Product();
        product.setBitCondition("qqqqqqqq");
//        List<BitInfo> infoList = new ArrayList<BitInfo>();
//        infoList.add(info);
//        product.setBitInfos(infoList);
//        
//        info.setProduct(product);
        
        dao.save(product);
        
        System.out.println("hello");
    }
}