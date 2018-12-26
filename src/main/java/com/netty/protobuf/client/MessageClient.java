package com.netty.protobuf.client;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.netty.protobuf.MsgReqProto;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class MessageClient {
	private final String host;
    private final int port;

    public MessageClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException, UnknownHostException {
    	new MessageClient("127.0.0.1",9908).start();
	}
    
    public void start() throws InterruptedException{
    		
    	EventLoopGroup loopGroup = new NioEventLoopGroup();
    	try {
   		 Bootstrap bootstrap = new Bootstrap(); //创建 Bootstrap          
   		 bootstrap.group(loopGroup)
            .channel(NioSocketChannel.class) //使用的 channel 类型是一个用于 NIO 传输
            .remoteAddress(new InetSocketAddress(host, port))//设置服务器的 InetSocketAddress
            .handler(new ChannelInitializer<SocketChannel>() {//当建立一个连接和一个新的通道时,创建添加到EchoClientHandler 实例 到 channel pipeline
           	 	
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast("decoder",new ProtobufDecoder(MsgReqProto.MsgReq.getDefaultInstance()));
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast("encoder",new ProtobufEncoder());
					ch.pipeline().addLast(new ClientMsgHandler());
				}
			});
   		
   		ChannelFuture f  = bootstrap.connect().sync(); //连接到远程,等待连接完成
   		f.channel().closeFuture().sync();//阻塞直到 Channel 关闭
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			loopGroup.shutdownGracefully().sync(); //调用 shutdownGracefully() 来关闭线程池和释放所有资源  
       }
    }
    
    
}
