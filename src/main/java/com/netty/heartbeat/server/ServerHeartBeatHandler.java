package com.netty.heartbeat.server;

import com.netty.heartbeat.message.HeartbeatMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHeartBeatHandler extends ChannelInboundHandlerAdapter{

	private int timeout = 0;  //超时次数
	
	private ServerMessageProcess smp = new ServerMessageProcess();
	/**
	 * 如果读超时触发次方法
	 */
	@Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
		if (evt instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) evt;  
            if (event.state() == IdleState.READER_IDLE) {  
            	timeout++;  
                System.out.println("10秒没有接收到客户端的信息了");  
                if (timeout > 2) {  //超时次数大于2,关闭这个不活跃的channel.
                    System.out.println("超时次数大于两次,关闭这个不活跃的channel连接");  
                    ctx.channel().close();  
                }  
            }  
        }else{
            super.userEventTriggered(ctx, evt);  
        }  
    }  

	/**
	 * channelRead() - 每个信息入站都会调用
	 */
	 @Override  
	 public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {  
	        if (obj instanceof HeartbeatMessage) {
	        	HeartbeatMessage msg = (HeartbeatMessage)obj;
	        	smp.messageProcess(ctx, msg);
			}	
	 }
	 
	 /**
	  * exceptionCaught()- 读操作时捕获到异常时调用
	  */
	 @Override  
	 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
	        cause.printStackTrace();  
	        ctx.close();  
	    }  
		
}
