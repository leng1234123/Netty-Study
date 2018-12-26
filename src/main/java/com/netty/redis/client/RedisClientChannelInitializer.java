package com.netty.redis.client;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class RedisClientChannelInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline =ch.pipeline();
		pipeline.addLast("decoder", new StringDecoder()); 
	  //  pipeline.addLast("encoder", new StringEncoder());  
	    pipeline.addLast("encoder", new RedisDataToStringEncoder());  
		pipeline.addLast(new RedisClientHandler());  
	}

}
