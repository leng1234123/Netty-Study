package com.netty.redis.client;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import com.netty.redis.msg.RedisCmd;
import com.netty.redis.msg.RedisMsg;
import com.netty.redis.service.RedisService;
import com.netty.redis.service.RedisServiceImpl;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.StringUtil;

public class RedisDataToStringEncoder extends MessageToMessageEncoder<RedisMsg> {

	private RedisService redisService = new RedisServiceImpl();
	
	@Override
	protected void encode(ChannelHandlerContext ctx, RedisMsg msg, List<Object> out) throws Exception {
		//数据解析为RESP协议命令格式
		String str = redisService.excuteCmd(msg);
		System.out.println("数据出站格式为"+str);
		if (StringUtil.isNullOrEmpty(str)) {			
			out.add(null);
		}else{
			out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(str),Charset.defaultCharset()));
		}
	}

}
