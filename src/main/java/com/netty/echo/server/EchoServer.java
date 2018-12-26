package com.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	   
	private final int port;

    public EchoServer(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException {
		
    	new EchoServer(9908).start();
    	
	}
    
    public void start() throws InterruptedException{
    	EventLoopGroup bossGroup = new NioEventLoopGroup(1); //负责处理客户端连接一个线程
    	EventLoopGroup workerGroup = new NioEventLoopGroup();//读写线程默认处理器数量 * 2
    	try {
			ServerBootstrap bootstrap = new ServerBootstrap();//创建 ServerBootstrap
			bootstrap.group(bossGroup,workerGroup)
			.channel(NioServerSocketChannel.class) //指定使用 NIO的传输Channel，调用bind方法时会进行初始化
			.localAddress(port) //设置 socket 地址使用所选的端口
			.childHandler(new ChannelInitializer<SocketChannel>() { //添加 EchoServerHandler 到 Channel 的 ChannelPipeline
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(
                            new EchoServerHandler());
				}
			});
			ChannelFuture f = bootstrap.bind().sync(); //绑定的服务器;sync 等待服务器关闭
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
    	} catch (Exception e) {
			e.printStackTrace();
		} finally{
			bossGroup.shutdownGracefully().sync();//关闭 EventLoopGroup，释放所有资源。
			workerGroup.shutdownGracefully().sync();
		}
    }
    
    
}
