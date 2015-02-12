package com.xinpinv.main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinpinv.pojo.BitInfo;


/**
 * 抢购线程类（按照特定的规则进行自动抢购）
 * @description 可以实现抢购指定次数后自动放弃
 * @author CHANSHUYI
 *
 */
public class AutoBitThread extends Thread {
	
	/** 本机IP **/
	private static final String myIp = "112.95.149.106";
	
	/** 抢购延迟时间（毫秒）**/
	private static final int delayTime = 30000;

	/** 在倒计时还有几毫秒内抢购**/
	private static final int timeLimit = 1750;  
	
	/** 最多出价几次就不出价  **/
	private static final int acceptCount = 15;
	
	/** AutoBit对象 **/
	private AutoBit autoBit;
	
	/** 竞购是否结束 **/
	public boolean isOver = false;
	
	/** 我的抢购次数 **/
	public static int myCount = 0;
		
	/** 最新的竞购用户 **/
	private String username = "default";
	
	/** 竞购总次数 **/
	public int count = -1;
	
	/** 竞购者IP **/
	public String ip = "0.0.0.0";
	
	/** 是否有人竞购 **/
	private boolean isChange = false; 
	
	private final int ServerDelayTime = 5000;  //服务器延迟时间（毫秒）
	
	private long sleepTime;
	
	public AutoBitThread()
	{
		super();
	}	
	
	public AutoBitThread(AutoBit autoBit)
	{
		this.autoBit = autoBit;
		this.sleepTime = 3 * 1000;	//抢购间隔
	}
	
	/**
	 * 抢购线程入口
	 */
	public void run()
	{
		System.out.println("===== 启动抢购线程 =====");

		try
		{
			String targetTimeStr = "2014-11-1 08:00:00";
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
		
		//只要竞购不结束就一直待命抢购
		while(!isOver)		
		{
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				System.out.println("################### 出价 " + format.format(new Date())  + " ###################");
				autoBit.bit();
				refreshInfo();
				System.out.println("已完成第[" + ++myCount + "]次抢购,现在抢购总次数为：" + count +  " ");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			while(true)
			{
				if(isOver)
				{
					System.out.println("=================================================================");
					System.out.println("=======================商品已竞购结束==============================");
					System.out.println("===========最终竞购者是：" + username + "==========================");
					System.out.println("===========竞购者IP是：" + ip + "==========================");
					System.out.println("===========总竞购次数为：" + count + "==========================");
					System.out.println("=================================================================");
					break;
				}
				
				if(!isChange)
				{
					break;
				}
				
				if(ip.equals(myIp))
				{
					System.out.println("[已是最高价],总抢购次数为：" + count);
				}
				else
				{
					System.out.println("其他用户在抢购,总抢购次数为：" + count);
				}
				
				System.out.println("线程休息：[" + sleepTime + "]毫秒");
				if(sleepTime > 0)
				{
					try {
						sleep(sleepTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("=============================================================");
				isChange = false;	//每次休眠完重置
				refreshInfo();
			}
		}
	}
	
	/**
	 * 刷新最新竞购者信息
	 */
	public void refreshInfo()
	{
		//当大于设定的最大竞购次数时，放弃竞购
		if(myCount > acceptCount)
		{
			isOver = true;
		}
		
		Map<String, Object> values = getLatestInfo();
		
		String newIp = values.get("ip").toString();
		String newUsername = values.get("username").toString();
		int newCount = ((Integer)values.get("count")).intValue();
		
		Date endDate = (Date)values.get("endTime");
		
		//获取商品最新的竞购结束时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long endMills = endDate.getTime();
		
		
		//获取现在的时间
		Date nowDate = new Date();
		long nowMills = nowDate.getTime();
		
		//上次的竞购者信息
		System.out.println("[old]:" + username + "," + ip + "," + count);
		//这次的竞购者信息
		System.out.println("[new]:" + newUsername + "," + newIp + "," + newCount);
		System.out.println("----");
				
		if(newCount != count)
		{
			//有最新的竞购者
			isChange = true;
			
			this.count = newCount;
			this.ip = newIp;
			this.username = newUsername;
			
			sleepTime = endMills - nowMills - timeLimit - ServerDelayTime; //5000是新品惠的平均出价延迟
			
			//打印准备抢购的时间
			System.out.println("现在时间：" + format.format(nowDate));
			System.out.println("最新商品竞购结束时间：" + format.format(endDate));
			System.out.println("线程准备抢购的时间：" + format.format(new Date(nowMills + sleepTime)));
		}
		else
		{
			//没其他用户抢购
			if(newIp.endsWith(myIp))
			{
				System.out.println("222222222222");
				//如果最高价者是自己，则不抢购，继续睡眠
				isChange = true;
				sleepTime = timeLimit/2 + 1000; //睡delayTime/2秒，看是否还有人抢购
				
				//打印准备抢购的时间
				System.out.println("现在时间：" + format.format(nowDate));
				System.out.println("结束时间：" + format.format(endDate));
				System.out.println("准备抢购的时间：" + format.format(new Date(nowMills + sleepTime)));
			}
			else
			{
				System.out.println("33333333333");
				System.out.println("--------------准备抢购!");
				//否则抢购
				isChange = false;
			}
		}
	}
	
	/**
	 * 获取最新的竞购信息
	 */
	public Map<String, Object> getLatestInfo()
	{
		Map<String, Object> values = new HashMap<String, Object>();
		
		String fullBitUrl = "http://121.201.7.67:80/item/topBuyer?item_id=" + autoBit.getProductId() + "&limit=1";
		
		List<BitInfo> infoList =  StratchUtil.getBitInfos(fullBitUrl);
		
		int i = 0;
		while(infoList.size() == 0)
		{
			i++;
			System.out.println("||||||||||||||| 第" + i + "次定时抢购失败,程序正在继续抢购 ||||||||||||");
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				autoBit.bit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			infoList =  StratchUtil.getBitInfos(fullBitUrl);
		}
			
		BitInfo bitInfo = infoList.get(0);
		
		//放入最新竞购者的IP
		String ip = bitInfo.getIp();
		values.put("ip", ip);
		
		//放入最新竞购者的用户名
		String username = bitInfo.getUsername();
		values.put("username", username);
		
		//放入结束时间
		Date startTime = bitInfo.getBitTime();
		long mills = startTime.getTime() + delayTime;
		startTime = new Date(mills);
		values.put("endTime", startTime);
		
		//放入总竞购次数
		int totalCount = (int)(bitInfo.getPrice() * 100);
		values.put("count", totalCount);
		
		System.out.println("最新竞购者:" + username + "," + bitInfo.getIp() + "," + bitInfo.getAddress() + "," + totalCount);
		
		return values;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
}
