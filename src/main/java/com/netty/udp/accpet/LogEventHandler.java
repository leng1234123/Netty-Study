package com.netty.udp.accpet;

import com.netty.udp.send.LogEvent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, LogEvent event) throws Exception {
		StringBuilder builder = new StringBuilder(); 
        builder.append(event.getReceived());
        builder.append(" [");
        builder.append(event.getSource().toString());
        builder.append("] [");
        builder.append(event.getLogfile());
        builder.append("] : ");
        builder.append(event.getMsg());
        //打印出 LogEvent 的数据
        System.out.println(builder.toString()); 
	}
	
	/**
	 * 在异常时,输出消息并关闭 channel
	 */
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
