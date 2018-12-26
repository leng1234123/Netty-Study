package com.netty.udp.accpet;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LogEventMonitor {
		
		private final Bootstrap bootstrap;
		private final EventLoopGroup group;
		
		public LogEventMonitor(InetSocketAddress address) {
			group = new NioEventLoopGroup();
			bootstrap = new Bootstrap();
			bootstrap.group(group)
					 .channel(NioDatagramChannel.class)
					 .option(ChannelOption.SO_BROADCAST,true)
					 .handler(new LogEvenChannelInitializer())
					 .localAddress(address);
		}
		
		public Channel bind() {
	        return bootstrap.bind().syncUninterruptibly().channel();  
	    }
		
		public void stop() {
	        group.shutdownGracefully();
	    }
		
		public static void main(String[] args) throws InterruptedException {
			
			LogEventMonitor monitor =new LogEventMonitor(new InetSocketAddress(8888));
			try {
				 Channel channel = monitor.bind();
		         System.out.println("LogEventMonitor running");
		         
		         channel.closeFuture().await();
			}finally {
				monitor.stop();
			}
		}
		
}
