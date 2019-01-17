package com.netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SimpleChatServer {

	private int port;

	public SimpleChatServer(int port) {
		this.port = port;
	}

	public static void main(String[] args) throws InterruptedException {

		new SimpleChatServer(9902).start();
	}

	public void start() throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap(); // (2)
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
					.childHandler(new SimpleChatServerInitializer()) // (4)
					.option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
			System.out.println("SimpleChatServer 启动了");
			// 绑定端口，开始接收进来的连接
			ChannelFuture f = bootstrap.bind(port).sync(); // (7)
			// 等待服务器 socket 关闭
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			System.out.println("SimpleChatServer 关闭了");
		}
	}

}
