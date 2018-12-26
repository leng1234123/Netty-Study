package com.netty.redis.service;

import java.util.HashMap;
import java.util.Map;

import com.netty.redis.msg.RedisCmd;
import com.netty.redis.msg.RedisMsg;

public class RedisServiceImpl implements RedisService {

	@Override
	public boolean getKey(String key) {
		
		return true;
	}

	@Override
	public boolean setKey(String key, Object value) {
		Map<String, Object> dataMap =new HashMap<>();
		dataMap.put(key, value);
		RedisMsg msg =new RedisMsg(RedisCmd.SET, dataMap);
		sendRedisMsg(msg);
		return true;
	}

	@Override
	public boolean setKey(Map<String, Object> dataMap) {
		RedisMsg msg =new RedisMsg(RedisCmd.SET, dataMap);
		sendRedisMsg(msg);
		return true;
	}

}
