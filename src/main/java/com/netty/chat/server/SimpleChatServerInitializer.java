package com.netty.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class SimpleChatServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// ByteBuf delimiter = Unpooled.copiedBuffer("\t".getBytes());
		// 解码分割器(解码的帧的最大长度,解码时是否去掉分隔符)
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(10, Delimiters.lineDelimiter()));
		pipeline.addLast("encoder", new StringEncoder());
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("handler", new SimpleChatServerHandler());
	}

}
