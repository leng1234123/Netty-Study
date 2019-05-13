package com.netty.protstack.handler;


import java.util.concurrent.TimeUnit;

import com.netty.protstack.message.BuildMessage;
import com.netty.protstack.message.Header;
import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端登陆请求
 * @author lengyul
 * @date 2019年3月20日 下午8:51:04
 */
public class LoginAuthReqHandler extends SimpleChannelInboundHandler {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 10; i++) {			
			ctx.writeAndFlush(BuildMessage.buildLoginReq()); // 握手连接			
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP) {
			String result =  message.getBody().toString();
			if (!"0".equals(result)) {
				ctx.close(); //握手失败
				return;
			}
			System.out.println("[Recivce Server msg] Login is ok :" + message.getBody());
		}	
		ctx.fireChannelRead(msg); // handler调用链，传递给下一个handler
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
	}
	
}
