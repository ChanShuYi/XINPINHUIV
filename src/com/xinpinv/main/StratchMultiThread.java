package com.xinpinv.main;

/**
 * 多线程爬虫获取数据
 * @author CHANSHUYI
 *
 */
public class StratchMultiThread extends Thread {
	
	private int startIndex;
	private int endIndex;
	
	
	public StratchMultiThread() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public StratchMultiThread(int startIndex, int endIndex) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}
	
	public void run()
	{
		try {
			StratchInfo.getAllProductInfo(startIndex, endIndex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		int totalCount = 708;
		int perCount = 70;
		int threadCount = totalCount/perCount + 1;
		
		for(int i = 0; i < threadCount; i++)
		{
			int startIndex = i * perCount + 1;
			int endIndex = (i + 1) * perCount;
			new StratchMultiThread(startIndex, endIndex).start();
		}
		
	}
}
