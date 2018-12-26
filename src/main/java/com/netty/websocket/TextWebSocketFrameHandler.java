package com.netty.websocket;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

@Sharable //可以被多个Channel共享
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	/**
	 * 每当从服务端收到新的客户端连接时，客户端的 Channel 存入 ChannelGroup 列表中
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception { // (2)
		Channel channel = ctx.channel();
		// 通知其他客户端
		channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + channel.remoteAddress() + " 加入"));
		channels.add(channel);
		System.out.println("Client:" + channel.remoteAddress() + "加入");
	}

	/**
	 * 每当从服务端收到客户端断开时，客户端的 Channel 自动从 ChannelGroup 列表中移除
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception { // (3)
		Channel incoming = ctx.channel();
		// 通知其他客户端
		channels.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开"));
		System.out.println("Client:" + incoming.remoteAddress() + "离开");
	}

	/**
	 * 每当从服务端读到客户端写入信息时，将信息转发给其他客户端的 Channel
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		Channel channel = ctx.channel();
		for (Channel ch : channels) {
			if (ch != channel) {
				ch.writeAndFlush(new TextWebSocketFrame("[" + ch.remoteAddress() + "]" + msg.text()));
			} else {
				//
				channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
			}
		}
	}

	/**
	 * 服务端监听到客户端活动
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception { // (5)
		Channel channel = ctx.channel();
		System.out.println("Client:" + channel.remoteAddress() + "在线");
	}

	/**
	 * 服务端监听到客户端不活动
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception { // (6)
		Channel channel = ctx.channel();
		System.out.println("Client:" + channel.remoteAddress() + "掉线");
	}

	/**
	 * IO错误或者处理器在处理事件时抛出的异常时
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		Channel channel = ctx.channel();
		System.out.println("Client:" + channel.remoteAddress() + "异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}

}
