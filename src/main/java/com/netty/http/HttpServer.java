package com.netty.http;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpServer {
	   
	   private final int port;
	   
	   public HttpServer(int port){
		   this.port = port;
	   }
	   
	   public static void main(String[] args) throws InterruptedException {
		  new HttpServer(8888).start();
	   }
	   
	   public void  start() throws InterruptedException{
		   
		   ServerBootstrap bootstrap =new ServerBootstrap();
		   NioEventLoopGroup loopGroup =new NioEventLoopGroup();//允许注册在事件循环期间处理以供以后选择的通道
		   bootstrap.group(loopGroup) //指定父（接受者）和子（客户端）的EventLoopGroup
		   .channel(NioServerSocketChannel.class) //指定使用 NIO 的传输 Channel
		   .childHandler(new ChannelInitializer<SocketChannel>() {
			   
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				System.out.println("initChannel ch:" + ch);
				ch.pipeline()
							.addLast("decoder", new HttpRequestDecoder()) //用于解码request
							.addLast("encoder",new HttpResponseEncoder()) //用于编码response
							.addLast("aggregator",new HttpObjectAggregator(512 * 1024))//消息聚合器（重要）,512 * 1024代表聚合的消息内容长度不超过512kb    
							.addLast("handler", new HttpHandler());
			}
		})
		.option(ChannelOption.SO_BACKLOG, 128) // determining the number of connections queued
        .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
		 
		bootstrap.bind(port).sync(); //绑定服务器端口,sync
	  }
	   
	   
	   
}
