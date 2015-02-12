package com.xinpinv.main;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xinpinv.http.HttpClientUtil;
import com.xinpinv.pojo.BitInfo;

/**
 * ���湤����
 * @author CHANSHUYI
 *
 */
public class StratchUtil {
	
	/**
	 * ���ݴ�������URL��ַץȡ��Ϣ��װ��BitInfo����
	 * @param url
	 * @return
	 */
	public static List<BitInfo> getBitInfos(String url)
	{	//"http://xinpinv.com/item/topBuyer?item_id=660&limit=100"
		List<BitInfo> bitInfoList = new ArrayList<BitInfo>();
		try
		{
			String data = HttpClientUtil.doGet(url);
			
//			System.out.println("--" + data);
			/* ץȡ�û���  */
			List<String> nameList = getAllStrs(data, "<span class=\\\"user\\\">", "<\\/span>");
			
			/* ץȡ�����۸� */
			List<String> priceList = getAllStrs(data, "\\u51fa\\u4ef7\\uffe5", "\\u5143");
			
			/* ץȡ����ʱ�� */
			List<Date> dateList = new ArrayList<Date>();
			String timeRegex = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
			Pattern p = Pattern.compile(timeRegex);
			Matcher m = p.matcher(data);
			while(m.find())
			{
				Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(m.group());
				dateList.add(date);
			}
			
			/* ץȡ�û�IP��ַ */
			List<String> ipList = new ArrayList<String>();
			String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
			p = Pattern.compile(ipRegex);
			m = p.matcher(data);
			while(m.find())
			{
				String ipStr = m.group();
				ipList.add(ipStr);
			}
			
			/* ץȡ�û���ַ */
			List<String> orginList = getAllStrs(data, "<span class=\\\"address-ip\\\">\\r\\n", "IP:");
			List<String> addrList = new ArrayList<String>();
			for(String str : orginList)
			{
				addrList.add(str.substring(1));
			}
			
			/* ������ƴװ��BitInfo���� */
			for(int i = 0; i < nameList.size(); i++)
			{
				String name = nameList.get(i);
				String price = priceList.get(i);
				Date date = dateList.get(i);
				String ip = "";
				if(ipList.size() != 0 && i < ipList.size())
				{
					ip = ipList.get(i);
				}
				String addr = "";
				if(addrList.size() != 0 && i < addrList.size())
				{
					addr = addrList.get(i);
				}
				
				/* û������id��product���� */
				BitInfo bitInfo = new BitInfo();
				bitInfo.setUsername(name);
				bitInfo.setPrice(Float.parseFloat(price));
				bitInfo.setBitTime(date);
				bitInfo.setIp(ip);
				bitInfo.setAddress(addr);
				
				bitInfoList.add(bitInfo);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return bitInfoList;
	}
	
	
	/**
	 * ���ݹؼ��ַ�������Ŀ���ַ���
	 * @content Դ�ַ���
	 * @param keyStr �ؼ��ַ���
	 * @ignoreCount �����ҵ��ַ����Ĵ���
	 * @return
	 */
	public static String getTargetStr(String content, String keyStr, int ignoreCount)
	{
		int flagIndex = content.indexOf(keyStr);
		int beginIndex = content.indexOf(">", flagIndex );
		
		for(int i = 0; i < ignoreCount; i++)
		{
			beginIndex = content.indexOf(">", beginIndex + 1);
		}
		
		int endIndex = content.indexOf("<", beginIndex);
		
		return content.substring(beginIndex + 1, endIndex);
	}
	
	/**
	 * ѭ����ȡ�ַ�������startStr��endStr֮��Ĳ���
	 * @param content ����
	 * @param startStr ��ʼ�ַ���
	 * @param endStr �����ַ���
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static List<String> getAllStrs(String content, String beginStr, String endStr) throws UnsupportedEncodingException
	{
		List<String> strList = new ArrayList<String>();
		int beginStrLen = beginStr.length();
		
		if(content.indexOf(beginStr, 0) == -1)
		{
			/* û�ж�Ӧ�Ŀ�ʼ�ַ��� */
			return strList;
		}
		
		int beginIndex = content.indexOf(beginStr, 0) + beginStrLen;
		int endIndex = content.indexOf(endStr, beginIndex);
		
		if(endIndex == -1)
		{
			/* û�ж�Ӧ�Ľ����ַ��� */
			return strList;
		}
		
		while(true)
		{
			String data = content.substring(beginIndex, endIndex).trim();
			
			if(data.contains("\\u"))
			{
				data = all2String(data);
//				System.out.println(data);
			}

			strList.add(data);
			
			/* �����ַ������� */
			if(content.indexOf(beginStr, beginIndex) == -1 || content.indexOf(endStr, beginIndex) == -1)
			{
				break;
			}
			
			beginIndex = content.indexOf(beginStr, beginIndex) + beginStrLen;
			endIndex = content.indexOf(endStr, beginIndex);
		}
		
		return strList;
	}
	
	/**
	 * ������Unicode�ַ����ַ���ת���������ַ���
	 * @param data
	 * @return
	 */
	public static String all2String(String data)
	{//\u6728\u8981\u7334\u6025
		String unicodeStr = "";
		String afterUnicode = "";
		while(data.contains("\\u"))
		{
			unicodeStr = data.substring(data.indexOf("\\u"), data.indexOf("\\u") + 6);
			afterUnicode = unicode2String(unicodeStr);
			data = data.replace(unicodeStr, afterUnicode);
		}
		
		return data;
	}
	
	/**
	 * ����Unicode�ַ���תΪ����
	 * @param unicodeStr
	 * @return
	 */
	public static String unicode2String(String unicodeStr){  
	    StringBuffer sb = new StringBuffer();  
	    String str[] = unicodeStr.toUpperCase().split("\\\\U");
	    for(int i=0;i<str.length;i++){  
	      if(str[i].equals("")) continue;  
	      char c = (char)Integer.parseInt(str[i].trim(),16);  
	      sb.append(c);  
	    }  
	    return sb.toString();  
	}  

}
