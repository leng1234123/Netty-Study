package com.netty.protstack.handler;

import java.time.LocalDateTime;

import com.netty.protstack.message.BuildMessage;
import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatRespHandler extends SimpleChannelInboundHandler<NettyMessage> {

	private int timeout = 0; // 超时次数

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, NettyMessage msg) throws Exception {
		if (msg.getHeader().getType() == MessageType.HB_REQ) {
			System.out.println("[" + LocalDateTime.now() + "Server msg] : 接收到客户端" + ctx.channel().remoteAddress()
					+ "心跳数据 \n" + msg);
			NettyMessage message = BuildMessage.buildMessage(MessageType.HB_RESP, MessageType.BODY_OK);
			ctx.writeAndFlush(message);
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {  
            	timeout++;  
                System.out.println("10秒没有接收到客户端的信息了");  
                if (timeout > 2) {
                    System.out.println("超时次数大于两次,关闭这个不活跃的channel连接");  
                    ctx.channel().close();  
                }  
            }  
        }else{
            super.userEventTriggered(ctx, evt);  
        }
	}

}
