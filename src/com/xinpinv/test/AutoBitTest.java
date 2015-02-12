package com.xinpinv.test;

import org.junit.Test;

import com.xinpinv.main.AutoBit;
import com.xinpinv.main.AutoBitThread;
import com.xinpinv.main.CheckIsOverThread;

public class AutoBitTest {

	/**
	 * 测试自动竞购
	 */
	@Test
	public void test() {
		try{
			//1.用户登陆
			AutoBit autoBit2 = new AutoBit("971");
			autoBit2.login("654349752@qq.com", "12345678");
			//2.启动线程不断竞购
			Thread bitThread2 = new AutoBitThread(autoBit2);	
			bitThread2.start();	
			//3.检测竞购是否结束，如果结束自动停止竞购
			Thread checkThread2 = new CheckIsOverThread(bitThread2, "http://xinpinv.com/item/971");
			checkThread2.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
