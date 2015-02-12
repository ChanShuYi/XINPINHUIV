package com.xinpinv.main;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinpinv.http.HttpClientUtil;
import com.xinpinv.pojo.BitInfo;
import com.xinpinv.pojo.Product;
import com.xinpinv.service.BaseDao;

/**
 * 商品抢购信息抓取类
 * @description 抓取指定商品的抢购信息
 * @author CHANSHUYI
 *
 */
public class StratchInfo {
	
	public static void main(String args[]) throws Exception
	{
		getAllProductInfo(967, 968);
	}
	
	public static String getAllProductInfo(int startIndex, int endIndex) throws Exception
	{	
		/* 构造获取商品信息URL http://www.xinpinv.com/item  */
		String staticProductUrl =  "http://121.201.7.67:80/item/";	//商品URL
		String fullProductUrl = "";	//完整的商品URL
		String content = "";	//商品URL内容
		
		/* 构造获取商品竞拍信息的URL */
		String fullBitUrl = "";
		
		/* 抓取商品信息 */
		List<Product> productList = new ArrayList<Product>();
		
		/* 获取Spring容器里的基本DAO类进行save操作 */
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		BaseDao dao = (BaseDao)ctx.getBean("baseDao");
		
		//116 117 243  还未竞拍
		//271 272 错误 java.lang.IndexOutOfBoundsException
		for(int i = startIndex; i <= endIndex; i++)
		{
			
			System.out.println("------第[" + i + "]件商品------");
			/* 获取竞拍商品的商品信息 */
			fullProductUrl = staticProductUrl + i;
			content = HttpClientUtil.doGet(fullProductUrl);
			
			if(content.contains("哥，这里没有要找的东西啦"))
			{
				System.out.println("------第[" + i + "]件商品无数据------");
				continue;
			}
			
//			System.out.println("--正在获取第[" + i + "]件商品的商品数据");
//			System.out.println("--" + content);
			Product product = new Product();
			product.setTrueId(i);
			product.setProductName(getProductName(content));
			product.setFinalPrice(getFinalPrice(content));
			product.setMarketPrice(getMarketPrice(content));
			product.setBitCondition(getBitCondition(content));
			product.setDowncount(getDowncount(content));
			product.setFinalUser(getFinalUser(content));
			product.setBitCount(getBitCount(content));
			product.setBitRule(getBitRule(content));
//			System.out.println("--商品数据获取成功!");
			
//			System.out.println("---正在获取第[" + i + "]件商品的竞拍记录数据");
			/* 获取竞拍商品的竞拍记录 */
			fullBitUrl = "http://121.201.7.67:80/item/topBuyer?item_id=" + i + "&limit=2000";
			List<BitInfo> bitInfoList = StratchUtil.getBitInfos(fullBitUrl);
//			System.out.println("--商品竞拍记录数据获取成功!");
			
			/* 获取商品开始竞拍时间、结束竞拍时间、持续时间 */
			Date startTime = bitInfoList.get(bitInfoList.size() - 1).getBitTime();
			Date endTime = bitInfoList.get(0).getBitTime();
			long mins = (endTime.getTime() - startTime.getTime())/(1000 * 60); //分钟
			
			/* 设置商品的开始竞拍时间、结束竞拍时间、持续时间 */
			product.setStartTime(startTime);
			product.setEndTime(endTime);
			product.setDuration(mins);
			
			/* 设置Product-BitInfo双向关联 */
			for(BitInfo bitInfo : bitInfoList)
			{
				bitInfo.setProduct(product);
			}
			product.setBitInfos(bitInfoList);
			
			productList.add(product);
			
			dao.save(product);
//			System.out.println("--成功将数据存入数据库!");
		}
		
		return new String();
	}
	
	/**
	 * 获取商品名
	 * @param content
	 * @return
	 */
	public static String getProductName(String content)
	{
		return StratchUtil.getTargetStr(content, "product-name", 0);
	}
	
	/**
	 * 获取商品最终价格
	 * @param content
	 * @return
	 */
	public static float getFinalPrice(String content)
	{
		String priceStr = StratchUtil.getTargetStr(content, "data-price", 0);
		return Float.parseFloat(priceStr);
	}
	
	/**
	 * 获取商品的市场价格
	 * @param content
	 * @return
	 */
	public static float getMarketPrice(String content)
	{
		String productValueStr = StratchUtil.getTargetStr(content, "商品价值：", 1);
		return Float.parseFloat(productValueStr.substring(0, productValueStr.length() - 1));
	}
	
	/**
	 * 获取竞购资格
	 * @param content
	 * @return
	 */
	public static String getBitCondition(String content)
	{
		return StratchUtil.getTargetStr(content, "竞购资格：", 1);
	}
	
	/**
	 * 获取延时周期
	 * @param content
	 * @return
	 */
	public static int getDowncount(String content)
	{
		String downcountStr = StratchUtil.getTargetStr(content, "延时周期：", 1);
		return Integer.parseInt(downcountStr.substring(0, downcountStr.length() - 1));
	}
	
	/**
	 * 获取竞拍成功用户
	 * @param content
	 * @return
	 */
	public static String getFinalUser(String content)
	{
		return StratchUtil.getTargetStr(content, "　出价人：", 1);
	}
	
	/**
	 * 获取竞拍次数
	 * @param content
	 * @return
	 */
	public static int getBitCount(String content)
	{
		String bitCountStr = StratchUtil.getTargetStr(content, "出价人次：", 1);
		return Integer.parseInt(bitCountStr.substring(0, bitCountStr.length() - 2));
	}
	
	/**
	 * 获取出价规则
	 * @param content
	 * @return
	 */
	public static String getBitRule(String content)
	{
		return StratchUtil.getTargetStr(content, "出价规则：", 1);
	}
}
