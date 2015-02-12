package com.xinpinv.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	
	
	/**
	 * 用GET请求对应的url
	 * @param url 请求URL
	 * @throws IOException 
	 */
	public static String doGet(String url) throws IOException
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		
		String content = "";
		try
		{
			/* 发送GET请求 */
			HttpGet httpGet = new HttpGet(url);
			
//			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();//设置请求和传输超时时间
//			httpGet.setConfig(requestConfig);
			
			CloseableHttpResponse response = httpclient.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			content = EntityUtils.toString(entity);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			httpclient.close();
		}
		return new String(content.getBytes("iso-8859-1"), "utf-8");
	}
	

	/**
	 * 发送POST请求，返回获取的数据
	 * @param client
	 * @param url
	 * @param strs
	 * @return
	 * @throws Exception 
	 */
	public static String doPost(HttpClient client, String url, Map<String, String> values) throws Exception
	{
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(url);    
		
//		HttpHost proxy = new HttpHost("14.29.117.36", 80, "http");
//	    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
//	    httpPost.setConfig(config);
		
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();//设置请求和传输超时时间4
		httpPost.setConfig(requestConfig);
	    
		
		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(getParam(values), "UTF-8");
		httpPost.setEntity(postEntity);
		    		    
		    if(client == null)
		    {
		    	CloseableHttpClient httpclient = HttpClients.createDefault();
		    	//不需要登录的请求,用noLoginClient对象
		    	httpResponse = httpclient.execute(httpPost);
		    }
		    else
		    {
		    	httpResponse = client.execute(httpPost);
		    }
		    
		    String content = EntityUtils.toString(httpResponse.getEntity());
		    
//		    printHeaders(httpResponse);
//		    System.out.println("编码：" + httpResponse.getEntity().getContentEncoding());
//		    String test1 = new String(content.getBytes("utf-8"), "iso-8859-1");
		    
//		    return new String(content.getBytes("iso-8859-1"), "utf-8");
		    return content;
	}
	
	/**
	 * 根据传入的Map参数返回POST请求所需的Entity对象
	 */
	public static List<NameValuePair> getParam(Map<String, String> parameterMap) {
	    List<NameValuePair> param = new ArrayList<NameValuePair>();
	    Iterator<Map.Entry<String,String>> it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) {
	      Entry<String, String> parmEntry = (Entry<String, String>) it.next();
	      param.add(new BasicNameValuePair((String) parmEntry.getKey(),
	          (String) parmEntry.getValue()));
	    }
	    return param;
	}
	
	/**
	 * 打印HTTP返回头
	 * @param httpResponse
	 */
	public static void printHeaders(HttpResponse httpResponse)
	{
		HeaderIterator iterator = httpResponse.headerIterator();
	    while (iterator.hasNext()) {
	      System.out.println("\t" + iterator.next());
	    }
	}
}



























