package com.netty.redis;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.netty.redis.client.RedisClient;
import com.netty.redis.service.RedisService;
import com.netty.redis.service.RedisServiceImpl;

public class RedisTest {
	
	RedisService redisService = new RedisServiceImpl();
	@Test
	public void test() throws InterruptedException{
	
	 new Thread( () -> {
		 try {
			Thread.sleep(3000);
			Map<String,Object> dataMap =new HashMap<>();
			redisService.setKey(dataMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	 });	
	 
	 System.out.println("11111111111111111");
	 new RedisClient("127.0.0.1",6379).start();	
	 
	 	
	}
	
	
}
