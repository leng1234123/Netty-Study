package com.netty.protobuf.server;


import java.nio.charset.Charset;
import java.util.List;

import com.netty.protobuf.MsgReqProto.MsgReq;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 使用Protobuf转换字节为对象
 * @author lengyul
 * @date 2018年12月5日 下午3:36:33
 */
public class ServerMsgDecoder extends ByteToMessageDecoder {


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte [] bytes = new byte[in.readableBytes()];		
		in.readBytes(bytes);
		MsgReq msg = MsgReq.parseFrom(bytes);
		out.add(msg);
	}
	
	
	
}
	
