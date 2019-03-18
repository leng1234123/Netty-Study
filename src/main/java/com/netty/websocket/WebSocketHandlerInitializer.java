package com.netty.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketHandlerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("http-codec",new HttpServerCodec()); // http编解码
		pipeline.addLast("aggregator",new HttpObjectAggregator(65536)); // http消息组合
		pipeline.addLast("http-chunked",new ChunkedWriteHandler());  // 浏览器与服务端进行通信
		pipeline.addLast("handler",new WebSocketServerHandler()); // 服务端数据处理
		
	}

}
