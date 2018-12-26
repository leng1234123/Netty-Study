package com.netty.udp.accpet;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;

public class LogEvenChannelInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		 	ChannelPipeline pipeline = ch.pipeline();
		 	pipeline.addLast(new LogEventDecoder());
		 	pipeline.addLast(new LogEventHandler());
	}

}
