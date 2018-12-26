package com.netty.heartbeat.client;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class HeartbeatClient {
	 private final String host;
	    private final int port;

	    public HeartbeatClient(String host, int port) {
	        this.host = host;
	        this.port = port;
	    }
	    
	    public static void main(String[] args) throws InterruptedException, UnknownHostException {
	    	new HeartbeatClient("127.0.0.1",9901).start();
		}
	    
	    public void start() throws InterruptedException{
	    	EventLoopGroup loopGroup = new NioEventLoopGroup(); //使用NIO事件驱动为NioEventLoopGroup 
	    	try {
	    		 Bootstrap bootstrap = new Bootstrap(); //创建 Bootstrap          
	    		 bootstrap.group(loopGroup)
	             .channel(NioSocketChannel.class) //使用的 channel 类型是一个用于 NIO 传输
	             .remoteAddress(new InetSocketAddress(host, port))//设置服务器的 InetSocketAddress
	             .handler(new ClientHeartBeatHandlerInitializer());
	    		
	    		ChannelFuture f  = bootstrap.connect().sync(); //连接到远程,等待连接完成
	    		f.channel().closeFuture().sync();//阻塞直到 Channel 关闭
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				loopGroup.shutdownGracefully().sync(); //调用 shutdownGracefully() 来关闭线程池和释放所有资源  
	        }
	    	
	    }
}
