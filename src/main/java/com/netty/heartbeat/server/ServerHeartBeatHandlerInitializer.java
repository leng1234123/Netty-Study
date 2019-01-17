package com.netty.heartbeat.server;

import java.util.concurrent.TimeUnit;

import com.netty.heartbeat.message.ByteToMessageDecoder;
import com.netty.heartbeat.message.MessageDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerHeartBeatHandlerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		// 配置心跳(检测读超时时间 10s),出入站数据处理,Handler类
		// long readerIdleTime, long writerIdleTime, long allIdleTime,TimeUnit
		// unit
		// 每隔10s检测channelRead方法是否被调用,如果10s内channelRead方法没有被触发,就会调用userEventTriggered方法
		pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
	//	pipeline.addLast("decoder", new MessageDecoder(1024 , 1, 4, 0, 0, false));
		pipeline.addLast("decoder", new ByteToMessageDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast(new ServerHeartBeatHandler());
	}

}
