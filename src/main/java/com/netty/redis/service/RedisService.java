package com.netty.redis.service;

import java.util.Map;
import java.util.Map.Entry;

import com.netty.redis.RedisClientChannelUtils;
import com.netty.redis.msg.RedisCmd;
import com.netty.redis.msg.RedisMsg;

public interface RedisService {
	
	/**
	 * 将数据通过channel发送出去
	 * @param msg
	 */
	default void sendRedisMsg(RedisMsg msg){
		RedisClientChannelUtils.writeRedisMsg(msg);
	}
	
	/**
	 * 数据出站时执行
	 * 解析RedisMsg数据(生成RESP协议格式数据)
	 * @param msg
	 * @return
	 */
	default String excuteCmd(RedisMsg msg) {
		StringBuffer sb = new StringBuffer();
		int size = 0;
		Map<String, Object> dataMap = msg.getDataMap();
		RedisCmd cmd = msg.getCmd();
		switch (cmd) {
		case GET:
			size = dataMap.size() + 1;
			sb.append("*" + size + RedisMsg.CRLF + "$3" + RedisMsg.CRLF + "GET");
			if (dataMap.size() > 0) {	
				String key = (String) dataMap.get(0);
				sb.append(RedisMsg.CRLF + "$"+ key.length() + RedisMsg.CRLF + key + RedisMsg.CRLF);
			}
			break;
		case SET:
			size = dataMap.size() + 2;
			sb.append("*" + size + RedisMsg.CRLF + "$3" + RedisMsg.CRLF + "SET");
			if (dataMap.size() > 0) {			
				for (Entry<String, Object> str : dataMap.entrySet()) {
					String key = str.getKey().toString();
					String value = str.getValue().toString();
					sb.append(RedisMsg.CRLF + "$" + key.length() + RedisMsg.CRLF + key + RedisMsg.CRLF + "$" + value.length() + RedisMsg.CRLF
							+ value + RedisMsg.CRLF);
				}
			}
			break;
		default:
			break;
		}
		return sb.toString();		
	}
	
	boolean getKey(String key);
	
	boolean setKey(String key,Object value);
	
	boolean setKey(Map<String,Object> keyMap);
	
}
