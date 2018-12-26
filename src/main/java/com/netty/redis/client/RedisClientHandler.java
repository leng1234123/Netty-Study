package com.netty.redis.client;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.netty.redis.service.RedisService;
import com.netty.redis.service.RedisServiceImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RedisClientHandler extends SimpleChannelInboundHandler<String> {

	
	private RedisService redisService = new RedisServiceImpl();
	/**
	 * 建立连接后该 channelActive() 方法被调用一次
	 */
	@Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
		System.out.println("已建立连接,激活时间是:"+LocalDateTime.now()+"远程地址是:"+ctx.channel().remoteAddress().toString());  

		Map<String, Object> dataMap =new HashMap<>();
		dataMap.put("hello", "world");
		redisService.setKey(dataMap);
        // ctx.channel().writeAndFlush("*2\r\n$3\r\nGET\r\n$5\r\nhello\r\n");
		ctx.fireChannelActive();  
    }
	
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		System.out.println("接受到服务端返回数据："+msg);
	}
	
	public static void main(String[] args) {
		String str = "3\r\n$";
		System.out.println(str);
	}

}
