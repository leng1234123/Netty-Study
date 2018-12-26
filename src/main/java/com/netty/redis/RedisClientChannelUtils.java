package com.netty.redis;

import com.netty.redis.msg.RedisMsg;

import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;

public class RedisClientChannelUtils {
		
	public static Channel channel = null;
	
	public static void writeRedisMsg(RedisMsg msg){
		if (msg != null) {
			channel.writeAndFlush(msg);
		}
	}
	
	public static void writeStr(String str){
		if (StringUtil.isNullOrEmpty(str)) {			
			channel.writeAndFlush(str);
		}
	}

	
}
