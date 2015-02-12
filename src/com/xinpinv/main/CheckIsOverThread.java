package com.xinpinv.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xinpinv.http.HttpClientUtil;

/**
 * ����״̬�����
 * @author CHANSHUYI
 */
public class CheckIsOverThread extends Thread {
	
	private final long sleepTime = 120000;
	
	private AutoBitThread bitThread;	
		
	private boolean isOver = false;
		
	private String productUrl = "";
	
	private int count = 0;
	
	public CheckIsOverThread()
	{
		
	}
	
	public CheckIsOverThread(Thread bitThread, String productUrl)
	{
		this.bitThread = (AutoBitThread)bitThread;
		this.productUrl = productUrl;
	}
	
	/**
	 * ��龺���Ƿ����
	 */
	public void run()
	{
		try
		{
			String targetTimeStr = "2014-11-1 08:00:00";  //���ʱ��Ҫ��AutoBitThread���е�ʱ�䱣��һ��
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date targetTime = dateFormate.parse(targetTimeStr);
			System.out.println("����˯�ߣ��ȵ�" + targetTimeStr + "��");
			sleep(targetTime.getTime() - new Date().getTime());
			System.out.println("ʱ�䵽!��������!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		isOver = getIsOver();
		count++;
		System.out.println("*******[" + dateFormat.format(new Date()) + "]����龺���Ƿ������" + isOver + ",��[" + count + "]�μ�� ************");
		
		while(!isOver)
		{
			try {
				//�����Ӽ��һ��
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			isOver = getIsOver();
			count++;
			
			System.out.println("*******[" + dateFormat.format(new Date()) + "]����龺���Ƿ������" + isOver + ",��[" + count + "]�μ�� ************");
		}
		
		if(isOver)
		{
			bitThread.setOver(true);
			System.out.println("========CheckIsOverThrad:run() ---- ��Ʒ�Ѿ���������  ==========");
		}
		else
		{
			System.out.println("=====����Ƿ���������쳣!====");
		}
	}

	/**
	 * �����ȡ���µ�������Ϣ���ж������Ƿ����
	 * @return �����Ƿ����
	 */
	public boolean getIsOver() 
	{
		boolean result = false;
		
		try
		{			
			String content = HttpClientUtil.doGet(productUrl);
			
			if(content.contains("����������Ʒ"))
			{
				result = true; //��������
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result; //�������ڼ���
	}
// ������	
//	public static void main(String args[])
//	{
//		Thread thread = new CheckIsOverThread("http://xinpinv.com/item/955");
//		
//		thread.start();
//	}
}
