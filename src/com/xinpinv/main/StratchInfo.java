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
 * ��Ʒ������Ϣץȡ��
 * @description ץȡָ����Ʒ��������Ϣ
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
		/* �����ȡ��Ʒ��ϢURL http://www.xinpinv.com/item  */
		String staticProductUrl =  "http://121.201.7.67:80/item/";	//��ƷURL
		String fullProductUrl = "";	//��������ƷURL
		String content = "";	//��ƷURL����
		
		/* �����ȡ��Ʒ������Ϣ��URL */
		String fullBitUrl = "";
		
		/* ץȡ��Ʒ��Ϣ */
		List<Product> productList = new ArrayList<Product>();
		
		/* ��ȡSpring������Ļ���DAO�����save���� */
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		BaseDao dao = (BaseDao)ctx.getBean("baseDao");
		
		//116 117 243  ��δ����
		//271 272 ���� java.lang.IndexOutOfBoundsException
		for(int i = startIndex; i <= endIndex; i++)
		{
			
			System.out.println("------��[" + i + "]����Ʒ------");
			/* ��ȡ������Ʒ����Ʒ��Ϣ */
			fullProductUrl = staticProductUrl + i;
			content = HttpClientUtil.doGet(fullProductUrl);
			
			if(content.contains("�磬����û��Ҫ�ҵĶ�����"))
			{
				System.out.println("------��[" + i + "]����Ʒ������------");
				continue;
			}
			
//			System.out.println("--���ڻ�ȡ��[" + i + "]����Ʒ����Ʒ����");
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
//			System.out.println("--��Ʒ���ݻ�ȡ�ɹ�!");
			
//			System.out.println("---���ڻ�ȡ��[" + i + "]����Ʒ�ľ��ļ�¼����");
			/* ��ȡ������Ʒ�ľ��ļ�¼ */
			fullBitUrl = "http://121.201.7.67:80/item/topBuyer?item_id=" + i + "&limit=2000";
			List<BitInfo> bitInfoList = StratchUtil.getBitInfos(fullBitUrl);
//			System.out.println("--��Ʒ���ļ�¼���ݻ�ȡ�ɹ�!");
			
			/* ��ȡ��Ʒ��ʼ����ʱ�䡢��������ʱ�䡢����ʱ�� */
			Date startTime = bitInfoList.get(bitInfoList.size() - 1).getBitTime();
			Date endTime = bitInfoList.get(0).getBitTime();
			long mins = (endTime.getTime() - startTime.getTime())/(1000 * 60); //����
			
			/* ������Ʒ�Ŀ�ʼ����ʱ�䡢��������ʱ�䡢����ʱ�� */
			product.setStartTime(startTime);
			product.setEndTime(endTime);
			product.setDuration(mins);
			
			/* ����Product-BitInfo˫����� */
			for(BitInfo bitInfo : bitInfoList)
			{
				bitInfo.setProduct(product);
			}
			product.setBitInfos(bitInfoList);
			
			productList.add(product);
			
			dao.save(product);
//			System.out.println("--�ɹ������ݴ������ݿ�!");
		}
		
		return new String();
	}
	
	/**
	 * ��ȡ��Ʒ��
	 * @param content
	 * @return
	 */
	public static String getProductName(String content)
	{
		return StratchUtil.getTargetStr(content, "product-name", 0);
	}
	
	/**
	 * ��ȡ��Ʒ���ռ۸�
	 * @param content
	 * @return
	 */
	public static float getFinalPrice(String content)
	{
		String priceStr = StratchUtil.getTargetStr(content, "data-price", 0);
		return Float.parseFloat(priceStr);
	}
	
	/**
	 * ��ȡ��Ʒ���г��۸�
	 * @param content
	 * @return
	 */
	public static float getMarketPrice(String content)
	{
		String productValueStr = StratchUtil.getTargetStr(content, "��Ʒ��ֵ��", 1);
		return Float.parseFloat(productValueStr.substring(0, productValueStr.length() - 1));
	}
	
	/**
	 * ��ȡ�����ʸ�
	 * @param content
	 * @return
	 */
	public static String getBitCondition(String content)
	{
		return StratchUtil.getTargetStr(content, "�����ʸ�", 1);
	}
	
	/**
	 * ��ȡ��ʱ����
	 * @param content
	 * @return
	 */
	public static int getDowncount(String content)
	{
		String downcountStr = StratchUtil.getTargetStr(content, "��ʱ���ڣ�", 1);
		return Integer.parseInt(downcountStr.substring(0, downcountStr.length() - 1));
	}
	
	/**
	 * ��ȡ���ĳɹ��û�
	 * @param content
	 * @return
	 */
	public static String getFinalUser(String content)
	{
		return StratchUtil.getTargetStr(content, "�������ˣ�", 1);
	}
	
	/**
	 * ��ȡ���Ĵ���
	 * @param content
	 * @return
	 */
	public static int getBitCount(String content)
	{
		String bitCountStr = StratchUtil.getTargetStr(content, "�����˴Σ�", 1);
		return Integer.parseInt(bitCountStr.substring(0, bitCountStr.length() - 2));
	}
	
	/**
	 * ��ȡ���۹���
	 * @param content
	 * @return
	 */
	public static String getBitRule(String content)
	{
		return StratchUtil.getTargetStr(content, "���۹���", 1);
	}
}
