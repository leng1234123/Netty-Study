package com.netty.protstack.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.netty.protstack.message.BuildMessage;
import com.netty.protstack.message.Header;
import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 处理客户端登录响应
 * @author lengyul
 * @date 2019年3月21日 上午9:17:03
 */
public class LoginAuthRespHandler extends SimpleChannelInboundHandler{
	
	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
//	private String[] whiteList = {"127.0.0.1", "192.168.1.111"};
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		System.out.println("LoginAuthRespHandler:" + this);
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage resp = new NettyMessage();
			// 
			if (nodeCheck.containsKey(nodeIndex)) {
				System.out.println(Thread.currentThread().getName() + "[Server msg] channel"+nodeIndex+" 重复登陆认证");
				resp = BuildMessage.buildResponse(MessageType.BODY_FAIL);
			} else {
				System.out.println("[Server msg] channel"+nodeIndex+" 登陆认证成功");
				nodeCheck.put(nodeIndex, true);
				resp = BuildMessage.buildResponse(MessageType.BODY_OK);
			}
			ctx.writeAndFlush(resp);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		nodeCheck.remove(ctx.channel().remoteAddress().toString()); // 清除缓存IP
		ctx.close();
		/* super.exceptionCaught(ctx, cause); */
	}
	
	

}
