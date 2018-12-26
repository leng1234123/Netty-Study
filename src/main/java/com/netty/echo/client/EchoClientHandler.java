package com.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

/**
 * 实现客户端逻辑
 * @author lengyul
 *
 */
@Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf>{
		
	/**
	 * 建立连接后该 channelActive() 方法被调用一次
	 */
	 @Override
	 public void channelActive(ChannelHandlerContext ctx) {
	        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", 
	        CharsetUtil.UTF_8));
	    }
	
	 /**
	  * 接收到数据时被调用
	  */
	 @Override
	 public void channelRead0(ChannelHandlerContext ctx,
	        ByteBuf in) {
	        System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8));    //3
	 }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {                    
        cause.printStackTrace();
        ctx.close();
    }
}
