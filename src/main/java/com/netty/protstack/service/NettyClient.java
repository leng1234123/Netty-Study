package com.netty.protstack.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.netty.protstack.codec.NettyMessageDecoder;
import com.netty.protstack.codec.NettyMessgaeEncoder;
import com.netty.protstack.handler.HeartBeatReqHandler;
import com.netty.protstack.handler.LoginAuthReqHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect(String host, int port){
		
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
					pipeline.addLast(new NettyMessgaeEncoder());
					pipeline.addLast("LoginAuthReqHandler", new LoginAuthReqHandler());
					pipeline.addLast("HeartBeatReqHandler", new HeartBeatReqHandler());
				}
			});
		ChannelFuture future = null;
		try {
			future = b.connect(host, port).sync();
			System.out.println("Netty client started successfully. ip = "+future.channel().remoteAddress()+" port = " + port);
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			/*System.out.println("检测到连接断开，尝试断线重连中");
			executor.execute(() -> {
				try {
					TimeUnit.SECONDS.sleep(10);
					connect(host, port);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});*/
		}
		
	}
	
	public static void main(String[] args) {
		new NettyClient().connect("127.0.0.1", 8989);
	}
	
}
