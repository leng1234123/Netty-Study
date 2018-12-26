package com.netty.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 处理所有客户端连接及消息发送到其他客户端
 * @author lengyul
 *
 */
public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
	
	/**
	 * 通道组(保存所有连接的客户端进来Channel)
	 * ChannelGroup可管理服务器端所有的连接的Channel
	 */
	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	/**
	 * 每当从服务端收到新的客户端连接时客户端的Channel，存入ChannelGroup列表中
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {  // (2)
		Channel channel = ctx.channel();
		//通知列表中其他客户端Channel
		channels.writeAndFlush("[SERVER] - " + channel.remoteAddress() + " 加入\n");
		//当前客户端的 Channel 存入 ChannelGroup 列表中
		channels.add(ctx.channel());
	}
	
	/**
	 * 每当从服务端收到客户端断开时，客户端的 Channel自动从 ChannelGroup列表中移除
	 */
	 @Override
	 public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {  // (3)
		Channel channel = ctx.channel();
		//通知列表中其他客户端Channel
		channels.writeAndFlush("[SERVER] - " + channel.remoteAddress() + " 离开\n");
	 }
	 
	/**
	 * 每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		Channel channel = ctx.channel();
		for (Channel ch : channels) {
			//消息转发给其他Channel
            if (ch != channel){
            	//当前消息发送在自己的窗口
            	ch.writeAndFlush("[" + channel.remoteAddress() + "]" + msg + "\n");
            //为当前Channel时,自己发的消息
            } else {
            	ch.writeAndFlush("[you]" + msg + "\n");
            }
		}
	}
	
	
	/**
	 * 服务端监听到客户端活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
        Channel channel = ctx.channel();
		System.out.println("SimpleChatClient:"+channel.remoteAddress()+"已经连接,在线");
	}
	
	/**
	 * 服务端监听到客户端不活动
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
        Channel channel = ctx.channel();
		System.out.println("SimpleChatClient:"+channel.remoteAddress()+"断开连接,掉线");
	}
	
	/**
	 *  IO 错误或者处理器在处理事件时抛出的异常时
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (7)
	    	Channel incoming = ctx.channel();
			System.out.println("SimpleChatClient:"+incoming.remoteAddress()+"异常");
	        // 当出现异常就关闭连接
	        cause.printStackTrace();
	        ctx.close();
	    }
	
	

}
