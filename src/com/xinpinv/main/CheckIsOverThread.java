package com.xinpinv.main;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.xinpinv.http.HttpClientUtil;

/**
 * 抢购状态检查类
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
	 * 检查竞购是否结束
	 */
	public void run()
	{
		try
		{
			String targetTimeStr = "2014-11-1 08:00:00";  //这个时间要和AutoBitThread类中的时间保持一致
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date targetTime = dateFormate.parse(targetTimeStr);
			System.out.println("进入睡眠，等到" + targetTimeStr + "点");
			sleep(targetTime.getTime() - new Date().getTime());
			System.out.println("时间到!启动抢购!");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		isOver = getIsOver();
		count++;
		System.out.println("*******[" + dateFormat.format(new Date()) + "]：检查竞购是否结束：" + isOver + ",第[" + count + "]次检查 ************");
		
		while(!isOver)
		{
			try {
				//两分钟检查一次
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			isOver = getIsOver();
			count++;
			
			System.out.println("*******[" + dateFormat.format(new Date()) + "]：检查竞购是否结束：" + isOver + ",第[" + count + "]次检查 ************");
		}
		
		if(isOver)
		{
			bitThread.setOver(true);
			System.out.println("========CheckIsOverThrad:run() ---- 商品已经竞购结束  ==========");
		}
		else
		{
			System.out.println("=====检查是否结束出现异常!====");
		}
	}

	/**
	 * 请求获取最新的抢购信息，判断抢购是否结束
	 * @return 抢购是否结束
	 */
	public boolean getIsOver() 
	{
		boolean result = false;
		
		try
		{			
			String content = HttpClientUtil.doGet(productUrl);
			
			if(content.contains("竞购其他商品"))
			{
				result = true; //抢购结束
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result; //抢购还在继续
	}
// 测试用	
//	public static void main(String args[])
//	{
//		Thread thread = new CheckIsOverThread("http://xinpinv.com/item/955");
//		
//		thread.start();
//	}
}
