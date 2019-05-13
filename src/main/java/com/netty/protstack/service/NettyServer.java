package com.netty.protstack.service;

import java.util.concurrent.TimeUnit;

import com.netty.protstack.codec.NettyMessageDecoder;
import com.netty.protstack.codec.NettyMessgaeEncoder;
import com.netty.protstack.handler.HeartBeatRespHandler;
import com.netty.protstack.handler.LoginAuthRespHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServer {
	
	public void bind(int port) throws InterruptedException {
		//配置服务端NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
		.option(ChannelOption.SO_BACKLOG, 100) // 客户端连接等待请求队列
		.handler(new LoggingHandler(LogLevel.INFO)) // 日志打印
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new NettyMessageDecoder(1024, 4, 4));
				pipeline.addLast(new NettyMessgaeEncoder());
				pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
				pipeline.addLast("LoginAuthRespHandler", new LoginAuthRespHandler());
				pipeline.addLast("HeartBeatRespHandler", new HeartBeatRespHandler());
			}
		});
		ChannelFuture future = b.bind(port).sync();
		System.out.println("Netty server started successfully. port = " + port);
		future.channel().closeFuture().sync();
	}
	
	public static void main(String[] args) throws InterruptedException {
		new NettyServer().bind(8989);
	}
}
