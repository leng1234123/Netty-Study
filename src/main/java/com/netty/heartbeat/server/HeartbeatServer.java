package com.netty.heartbeat.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HeartbeatServer {
	
	private final int port;

    public HeartbeatServer(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException {
		
    	new HeartbeatServer(9901).start();
	}
    
    public void start() throws InterruptedException{
    	EventLoopGroup loopGroup = new NioEventLoopGroup();
    	try {
    		ServerBootstrap bootstrap = new ServerBootstrap();
    		bootstrap.group(loopGroup)
    		.channel(NioServerSocketChannel.class)
    		.localAddress(port)
    		.childHandler(new ServerHeartBeatHandlerInitializer())
    		.option(ChannelOption.SO_BACKLOG, 128)     
            .childOption(ChannelOption.SO_KEEPALIVE, true);
    		
            // 绑定端口开始接收进来的连接  
            ChannelFuture future = bootstrap.bind().sync();  
            System.out.println(HeartbeatServer.class.getName() + " started and listen on " + future.channel().localAddress());
            future.channel().closeFuture().sync();  
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			loopGroup.shutdownGracefully().sync();//关闭 EventLoopGroup释放所有资源
		}
    }
	
}
