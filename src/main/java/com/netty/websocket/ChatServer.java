package com.netty.websocket;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class ChatServer {
	
	private int port;

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
    	EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
    	try {
    		ServerBootstrap bs = new ServerBootstrap();
    		bs.group(bossGroup, workerGroup)
    		.channel(NioServerSocketChannel.class)
    		.childHandler(new ChatServerInitializer())
    		.option(ChannelOption.SO_BACKLOG, 128)
    		.childOption(ChannelOption.SO_KEEPALIVE, true);
    		
    		System.out.println("WebsocketChatServer 启动了");
            ChannelFuture f = bs.bind(port).sync();
            f.channel().closeFuture().sync();
		} finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
    		System.out.println("WebsocketChatServer 关闭了");
        }
    }

    public static void main(String[] args) throws Exception{
       
        new ChatServer(8888).start();
       
    }
}
