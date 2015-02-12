package com.xinpinv.main;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xinpinv.pojo.BitInfo;


/**
 * �����߳��ࣨ�����ض��Ĺ�������Զ�������
 * @description ����ʵ������ָ���������Զ�����
 * @author CHANSHUYI
 *
 */
public class AutoBitThread extends Thread {
	
	/** ����IP **/
	private static final String myIp = "112.95.149.106";
	
	/** �����ӳ�ʱ�䣨���룩**/
	private static final int delayTime = 30000;

	/** �ڵ���ʱ���м�����������**/
	private static final int timeLimit = 1750;  
	
	/** �����ۼ��ξͲ�����  **/
	private static final int acceptCount = 15;
	
	/** AutoBit���� **/
	private AutoBit autoBit;
	
	/** �����Ƿ���� **/
	public boolean isOver = false;
	
	/** �ҵ��������� **/
	public static int myCount = 0;
		
	/** ���µľ����û� **/
	private String username = "default";
	
	/** �����ܴ��� **/
	public int count = -1;
	
	/** ������IP **/
	public String ip = "0.0.0.0";
	
	/** �Ƿ����˾��� **/
	private boolean isChange = false; 
	
	private final int ServerDelayTime = 5000;  //�������ӳ�ʱ�䣨���룩
	
	private long sleepTime;
	
	public AutoBitThread()
	{
		super();
	}	
	
	public AutoBitThread(AutoBit autoBit)
	{
		this.autoBit = autoBit;
		this.sleepTime = 3 * 1000;	//�������
	}
	
	/**
	 * �����߳����
	 */
	public void run()
	{
		System.out.println("===== ���������߳� =====");

		try
		{
			String targetTimeStr = "2014-11-1 08:00:00";
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
		
		//ֻҪ������������һֱ��������
		while(!isOver)		
		{
			try
			{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				System.out.println("################### ���� " + format.format(new Date())  + " ###################");
				autoBit.bit();
				refreshInfo();
				System.out.println("����ɵ�[" + ++myCount + "]������,���������ܴ���Ϊ��" + count +  " ");
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
					System.out.println("=======================��Ʒ�Ѿ�������==============================");
					System.out.println("===========���վ������ǣ�" + username + "==========================");
					System.out.println("===========������IP�ǣ�" + ip + "==========================");
					System.out.println("===========�ܾ�������Ϊ��" + count + "==========================");
					System.out.println("=================================================================");
					break;
				}
				
				if(!isChange)
				{
					break;
				}
				
				if(ip.equals(myIp))
				{
					System.out.println("[������߼�],����������Ϊ��" + count);
				}
				else
				{
					System.out.println("�����û�������,����������Ϊ��" + count);
				}
				
				System.out.println("�߳���Ϣ��[" + sleepTime + "]����");
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
				isChange = false;	//ÿ������������
				refreshInfo();
			}
		}
	}
	
	/**
	 * ˢ�����¾�������Ϣ
	 */
	public void refreshInfo()
	{
		//�������趨����󾺹�����ʱ����������
		if(myCount > acceptCount)
		{
			isOver = true;
		}
		
		Map<String, Object> values = getLatestInfo();
		
		String newIp = values.get("ip").toString();
		String newUsername = values.get("username").toString();
		int newCount = ((Integer)values.get("count")).intValue();
		
		Date endDate = (Date)values.get("endTime");
		
		//��ȡ��Ʒ���µľ�������ʱ��
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		long endMills = endDate.getTime();
		
		
		//��ȡ���ڵ�ʱ��
		Date nowDate = new Date();
		long nowMills = nowDate.getTime();
		
		//�ϴεľ�������Ϣ
		System.out.println("[old]:" + username + "," + ip + "," + count);
		//��εľ�������Ϣ
		System.out.println("[new]:" + newUsername + "," + newIp + "," + newCount);
		System.out.println("----");
				
		if(newCount != count)
		{
			//�����µľ�����
			isChange = true;
			
			this.count = newCount;
			this.ip = newIp;
			this.username = newUsername;
			
			sleepTime = endMills - nowMills - timeLimit - ServerDelayTime; //5000����Ʒ�ݵ�ƽ�������ӳ�
			
			//��ӡ׼��������ʱ��
			System.out.println("����ʱ�䣺" + format.format(nowDate));
			System.out.println("������Ʒ��������ʱ�䣺" + format.format(endDate));
			System.out.println("�߳�׼��������ʱ�䣺" + format.format(new Date(nowMills + sleepTime)));
		}
		else
		{
			//û�����û�����
			if(newIp.endsWith(myIp))
			{
				System.out.println("222222222222");
				//�����߼������Լ���������������˯��
				isChange = true;
				sleepTime = timeLimit/2 + 1000; //˯delayTime/2�룬���Ƿ���������
				
				//��ӡ׼��������ʱ��
				System.out.println("����ʱ�䣺" + format.format(nowDate));
				System.out.println("����ʱ�䣺" + format.format(endDate));
				System.out.println("׼��������ʱ�䣺" + format.format(new Date(nowMills + sleepTime)));
			}
			else
			{
				System.out.println("33333333333");
				System.out.println("--------------׼������!");
				//��������
				isChange = false;
			}
		}
	}
	
	/**
	 * ��ȡ���µľ�����Ϣ
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
			System.out.println("||||||||||||||| ��" + i + "�ζ�ʱ����ʧ��,�������ڼ������� ||||||||||||");
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
		
		//�������¾����ߵ�IP
		String ip = bitInfo.getIp();
		values.put("ip", ip);
		
		//�������¾����ߵ��û���
		String username = bitInfo.getUsername();
		values.put("username", username);
		
		//�������ʱ��
		Date startTime = bitInfo.getBitTime();
		long mills = startTime.getTime() + delayTime;
		startTime = new Date(mills);
		values.put("endTime", startTime);
		
		//�����ܾ�������
		int totalCount = (int)(bitInfo.getPrice() * 100);
		values.put("count", totalCount);
		
		System.out.println("���¾�����:" + username + "," + bitInfo.getIp() + "," + bitInfo.getAddress() + "," + totalCount);
		
		return values;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
}
