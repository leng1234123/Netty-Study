package com.netty.protobuf.server;




import com.netty.echo.server.EchoServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MessageServer {
		
	private final int port;

    public MessageServer(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException {
    	
    	new MessageServer(9908).start();
    	
	}
    
    public void start() throws InterruptedException{
    	EventLoopGroup loopGroup = new NioEventLoopGroup(); //创建 EventLoopGroup
    	try {
			ServerBootstrap bootstrap = new ServerBootstrap();//创建 ServerBootstrap
			bootstrap.group(loopGroup)
			.channel(NioServerSocketChannel.class) //指定使用 NIO 的传输 Channel
			.localAddress(port) //设置 socket 地址使用所选的端口
			.childHandler(new ServerMsgHandlerInitializer());
			
			ChannelFuture f = bootstrap.bind().sync(); //绑定的服务器;sync 等待服务器关闭
			System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
    	} catch (Exception e) {
			e.printStackTrace();
		} finally{
			loopGroup.shutdownGracefully().sync();//关闭 EventLoopGroup，释放所有资源。
		}
    }
    
}
