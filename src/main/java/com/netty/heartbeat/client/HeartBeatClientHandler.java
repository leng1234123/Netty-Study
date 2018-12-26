package com.netty.heartbeat.client;

import java.time.LocalDateTime;

import com.netty.heartbeat.message.HeartbeatMsg;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {
	
	/*private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",  
            CharsetUtil.UTF_8));*/  
      
    private static final int TRY_TIMES = 3;  //控制心跳次数
      
    private int currentTime = 0;  
	
    private HeartbeatMsg msg = new HeartbeatMsg();
    /**
	 * 建立连接后该 channelActive() 方法被调用一次
	 */
	@Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
		System.out.println("已建立连接,激活时间是:"+LocalDateTime.now()+"远程地址是:"+ctx.channel().remoteAddress().toString());  
        ctx.fireChannelActive();  
        /*当一个channelPipeline中有多个channelHandler,
                     如果channelHandler有同样的方法时,只会调用一次,
                     如果需要后续的channelHandler调用需要调用fire开头的方法
        */
    }
	
	/**
	 * 客户端在写空闲超时时,发起一次心跳
	 */
	 @Override  
	 public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
	        System.out.println("循环触发时间:"+LocalDateTime.now());  
	        if (evt instanceof IdleStateEvent) {  
	            IdleStateEvent event = (IdleStateEvent) evt;  
	            if (event.state() == IdleState.WRITER_IDLE) {  
	                    currentTime++;  
	                    String data = new String("第"+currentTime+"次心跳");
	                    msg.setType((byte)0xA1);
	                    msg.setFlag((byte)0xB1);
	                    msg.setLength(data.length());
	                    msg.setData(data);
	                    ctx.channel().writeAndFlush(msg);
	            }  
	        }  
	    }  
	
	
    @Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
        System.out.println("停止时间是:"+LocalDateTime.now());  
        System.out.println("HeartBeatClientHandler channelInactive");  
    }  
}
