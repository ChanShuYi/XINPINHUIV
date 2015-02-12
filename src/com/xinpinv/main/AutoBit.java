package com.xinpinv.main;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.xinpinv.http.HttpClientUtil;

/**
 * �Զ������ࣨ��½�����������߳��ࣩ
 * @author CHANSHUYI
 * @date 2014-10-12
 *
 */
public class AutoBit {
	
	/** Ҫ���ĵ���ƷID **/
	private String productId;
	
	/** ��¼���� **/
	private CloseableHttpClient loginClient;
	
	/** ���¼���� **/
	private CloseableHttpClient noLoginClient = HttpClients.createDefault();
	
	/** ��¼URL **/
	private String loginUrl = "http://xinpinv.com/site/login";
	
	/** ����URL **/
	private String bitUrl = "http://xinpinv.com/item/bid?item_id=";
	
	/**
	 * ���췽��
	 * @param productId ��������Ʒ��ID
	 * �磺������Ʒ��ַΪhttp://xinpinv.com/item/bid?item_id=796����ô����ƷID��Ϊ796
	 */
	public AutoBit(int productId) {
		this.productId = productId + "";
		this.bitUrl = this.bitUrl + productId;
	}
	
	/**
	 * ���췽��
	 * @param productUrl ��������Ʒ��URL��ַ
	 * �磺http://xinpinv.com/item/bid?item_id=796
	 */
	public AutoBit(String productUrl) {
		this.productId = productUrl.substring(productUrl.lastIndexOf("=") + 1);
		this.bitUrl = this.bitUrl + productId;
	}
	
	/**
	 * ģ���¼
	 * @param username �û���
	 * @param password ����
	 * @throws Exception 
	 */
	public void login(String username, String password) throws Exception
	{
		loginClient = HttpClients.createDefault();     
			
	    Map<String, String> values = new HashMap<String, String>();
	    values.put("LoginForm[username]", username);
	    values.put("LoginForm[password]", password);
	    values.put("LoginForm[rememberMe]", "0");           
	    values.put("yt0", "��¼");

	    HttpClientUtil.doPost(loginClient, loginUrl, values);
	    
	    System.out.println("===== [��¼�ɹ�!] =====");
	}
	
	/**
	 * ����һ������
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
	 * ��ȡʵʱ���ݣ������жϾ����Ƿ������
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
