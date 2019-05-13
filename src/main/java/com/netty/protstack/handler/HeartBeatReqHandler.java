package com.netty.protstack.handler;

import com.netty.protstack.message.BuildMessage;
import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;
import com.netty.protstack.thread.HeartBeatReqThread;
import com.netty.protstack.thread.HeartBeatThreadExecutor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.ScheduledFuture;

public class HeartBeatReqHandler extends SimpleChannelInboundHandler<NettyMessage>{

	private volatile ScheduledFuture<?> heartbeatScheduled; 
	// HeartBeatReqThread heartBeatReqThread = null;

	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
		Integer result = Integer.parseInt(msg.getBody().toString());
		if (msg.getHeader().getType() == MessageType.LOGIN_RESP  && result == 0) {
			// LoginAuthReqHandler 登录响应成功，传递到当前handler，启动心跳线程
			// 初始化心跳请求配置信息
			HeartBeatThreadExecutor.setCtx(ctx);
			HeartBeatThreadExecutor.execute();
			
			// Netty NioEventLoop
			/*heartbeatScheduled = ctx.executor()
				.scheduleAtFixedRate(() -> {
					ctx.writeAndFlush(BuildMessage.buildMessage(MessageType.HB_REQ, MessageType.BODY_OK));
				}, 0, 5, TimeUnit.SECONDS);*/
			
		}else if (msg.getHeader().getType() == MessageType.HB_RESP && result == 0) {
			System.out.println("[Recivce Server msg] : 心跳响应 " + result + "，心跳正常");		
		} else if(msg.getHeader().getType() == MessageType.HB_RESP && result == 1) {
			System.out.println("[Recivce Server msg] : 心跳响应 " + result + "，尝试停止发送心跳");
			HeartBeatThreadExecutor.setStatus(false); 
		} else {			
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		/*if (heartbeatScheduled != null) {
			heartbeatScheduled.cancel(true);
			heartbeatScheduled = null;
		}*/
		if (!HeartBeatThreadExecutor.isShutdown()) {	
			HeartBeatThreadExecutor.setStatus(false); 
		}
		/*ctx.fireExceptionCaught(cause);*/
	}
}
