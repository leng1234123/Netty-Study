package com.netty.redis.client;

import java.net.UnknownHostException;

import com.netty.heartbeat.server.HeartbeatServer;
import com.netty.heartbeat.server.ServerHeartBeatHandlerInitializer;
import com.netty.redis.RedisClientChannelUtils;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class RedisClient {
	
	private final String host;
    private final int port;

    public RedisClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException, UnknownHostException {
    	new RedisClient("127.0.0.1",6379).start();
    	
    	
	}
    
    public void start() throws InterruptedException{
    	EventLoopGroup loopGroup = new NioEventLoopGroup(); //使用NIO事件驱动为NioEventLoopGroup 
    	try {
    		Bootstrap bootstrap = new Bootstrap();
    		bootstrap.group(loopGroup)
    		.channel(NioSocketChannel.class) 
    		.remoteAddress(host, port)
    		.handler(new RedisClientChannelInitializer());
    		
    		ChannelFuture future  = bootstrap.connect().sync(); //连接到远程,等待连接完成
    		System.out.println(RedisClient.class.getName() + " started and listen on " + future.channel().localAddress());
    		RedisClientChannelUtils.channel = future.channel(); //保存全局channel
    		future.channel().closeFuture().sync();//阻塞直到 Channel 关闭
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
			loopGroup.shutdownGracefully().sync(); //调用 shutdownGracefully() 来关闭线程池和释放所有资源  
        }
    }
    
}
