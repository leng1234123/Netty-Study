package com.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketServer {
	
	public void run(int port) throws InterruptedException {
		EventLoopGroup boosGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boosGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new WebSocketHandlerInitializer());
			Channel ch = b.bind(port).sync().channel();
			System.out.println("Web socket server started at port " + port);
			ch.closeFuture().sync();
		} finally {
			boosGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		try {
			new WebSocketServer().run(port);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
