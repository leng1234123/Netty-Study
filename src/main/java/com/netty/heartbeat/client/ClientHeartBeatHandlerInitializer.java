package com.netty.heartbeat.client;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientHeartBeatHandlerInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//配置心跳(发送心跳时间8s),出入站数据处理,Handler类
		pipeline.addLast("ping",new IdleStateHandler(0,8,0,TimeUnit.SECONDS));
		pipeline.addLast("decoder", new StringDecoder());  
		pipeline.addLast("encoder", new ClientMsgEncoder());  
		pipeline.addLast(new HeartBeatClientHandler());  
	}

}
