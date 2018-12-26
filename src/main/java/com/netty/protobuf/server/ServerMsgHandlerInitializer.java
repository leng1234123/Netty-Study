package com.netty.protobuf.server;


import com.netty.protobuf.MsgReqProto;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * ChannelInitializer初始化ChannelPipeline 中的 LogEventHandler
 * Netty 通过添加多个 LogEventHandler
 * 支持多个协议
 * @author lengyul
 *
 */
public class ServerMsgHandlerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		 ChannelPipeline pipeline = ch.pipeline();
		 
		 /*Socket客户端发送数据处理*/
		 pipeline.addLast(new ProtobufVarint32FrameDecoder());//decode前解决半包和粘包问题
		 pipeline.addLast("decoder",new ProtobufDecoder(MsgReqProto.MsgReq.getDefaultInstance()));
		 pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());//序列化字节数组长度
		 pipeline.addLast("encoder",new ProtobufEncoder()); 
	     pipeline.addLast(new ServerMsgHandler());
	}
}
	
