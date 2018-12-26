package com.netty.echo.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandler.Sharable;

/**
 * 负责连接创建后和接收到信息后该如何处理
 * 实现ChannelInboundHandlerAdapter 接口,用来定义处理入站事件的方法
 * @author lengyul
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter{

	/**
	 * channelRead() - 每个信息入站都会调用
	 */
	@Override
    public void channelRead(ChannelHandlerContext ctx,
        Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));//日志消息输出到控制台
        
        String sendMsg = "Netty Server is  OK!";
        ctx.write(Unpooled.copiedBuffer(sendMsg, 
    	        CharsetUtil.UTF_8));                            //将所接收的消息返回给发送者
    }
	
	/**
	 * channelReadComplete() - 通知处理器最后的 channelRead() 是当前批处理中的最后一条消息时调用
	 */
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)//冲刷所有待审消息到远程节点
        .addListener(ChannelFutureListener.CLOSE);
    }
	
	/**
	 * exceptionCaught()- 读操作时捕获到异常时调用
	 */
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx,
        Throwable cause) {
        cause.printStackTrace();                //打印异常堆栈跟踪
        ctx.close();                            //关闭通道
    }
}
