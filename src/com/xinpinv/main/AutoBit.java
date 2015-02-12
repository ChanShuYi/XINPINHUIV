package com.xinpinv.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.xinpinv.http.HttpClientUtil;

/**
 * 自动抢购类（登陆并开启抢购线程类）
 * @author CHANSHUYI
 * @date 2014-10-12
 *
 */
public class AutoBit {
	
	/** 要竞拍的商品ID **/
	private String productId;
	
	/** 登录对象 **/
	private CloseableHttpClient loginClient;
	
	/** 免登录对象 **/
	private CloseableHttpClient noLoginClient = HttpClients.createDefault();
	
	/** 登录URL **/
	private String loginUrl = "http://xinpinv.com/site/login";
	
	/** 抢购URL **/
	private String bitUrl = "http://xinpinv.com/item/bid?item_id=";
	
	/**
	 * 构造方法
	 * @param productId 所抢购商品的ID
	 * 如：抢购商品地址为http://xinpinv.com/item/bid?item_id=796，那么该商品ID即为796
	 */
	public AutoBit(int productId) {
		this.productId = productId + "";
		this.bitUrl = this.bitUrl + productId;
	}
	
	/**
	 * 构造方法
	 * @param productUrl 所抢购商品的URL地址
	 * 如：http://xinpinv.com/item/bid?item_id=796
	 */
	public AutoBit(String productUrl) {
		this.productId = productUrl.substring(productUrl.lastIndexOf("=") + 1);
		this.bitUrl = this.bitUrl + productId;
	}
	
	/**
	 * 模拟登录
	 * @param username 用户名
	 * @param password 密码
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception
	{
		loginClient = HttpClients.createDefault();     
			
	    Map<String, String> values = new HashMap<String, String>();
	    values.put("LoginForm[username]", username);
	    values.put("LoginForm[password]", password);
	    values.put("LoginForm[rememberMe]", "0");           
	    values.put("yt0", "登录");

	    HttpClientUtil.doPost(loginClient, loginUrl, values);
	    
	    System.out.println("===== [登录成功!] =====");
	}
	
	/**
	 * 进行一次抢购
	 * @throws Exception 
	 */
	public void bit() throws Exception
	{
	      Map<String, String> values = new HashMap<String, String>();
		  values.put("item_id", productId);
		  values.put("price", "100");                
		    
		  HttpClientUtil.doPost(loginClient, bitUrl, values);
	}

	/**
	 * 获取实时数据（用于判断竞拍是否结束）
	 * @throws Exception 
	 */
	public void getRTInfo() throws Exception
	{
		String refreshInfo = "http://xinpinv.com/item/refleshInfo";
		
		Map<String, String> values = new HashMap<String, String>();
		
		values.put("ids[]", productId);
		
		String resultStr =  HttpClientUtil.doPost(null, refreshInfo, values);
	
		System.out.println(resultStr);
	}
	
	/************************* GET/SET ***********************/
	public CloseableHttpClient getLoginClient() {
		return loginClient;
	}

	public void setLoginClient(CloseableHttpClient loginClient) {
		this.loginClient = loginClient;
	}

	public CloseableHttpClient getNoLoginClient() {
		return noLoginClient;
	}

	public void setNoLoginClient(CloseableHttpClient noLoginClient) {
		this.noLoginClient = noLoginClient;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getBitUrl() {
		return bitUrl;
	}

	public void setBitUrl(String bitUrl) {
		this.bitUrl = bitUrl;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
