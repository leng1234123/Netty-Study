package com.netty.spdy;

import java.net.InetSocketAddress;

import javax.net.ssl.SSLContext;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SpdyServer {
	   
	private final NioEventLoopGroup group = new NioEventLoopGroup();
	private final SSLContext context;
	private Channel channel;
 
	public SpdyServer(SSLContext context) {
		this.context = context;
	}
	
	public static void main(String[] args) {
		SSLContext context = null;//SecureChatSslContextFactory.getServerContext();
		final SpdyServer endpoint = new SpdyServer(context);
		ChannelFuture future = endpoint.start(new InetSocketAddress(4096));
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				endpoint.destroy();
			}
		});
		future.channel().closeFuture().syncUninterruptibly();
	}

	
	public ChannelFuture start(InetSocketAddress address) {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(group).channel(NioServerSocketChannel.class)
				.childHandler(new SpdyChannelInitializer(context));
		ChannelFuture future = bootstrap.bind(address);
		future.syncUninterruptibly();
		channel = future.channel();
		return future;
	}
	
	public void destroy() {
		if (channel != null) {
			channel.close();
		}
		group.shutdownGracefully();
	}

	
	

}
