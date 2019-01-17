package com.netty.heartbeat.message;

import java.nio.charset.Charset;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class ByteToMessageDecoder extends io.netty.handler.codec.ByteToMessageDecoder {

	private final int HEADER_SIZE = 5;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in == null) {
			return;
		}
		// 如果可读字节数小于头部信息
		if (in.readableBytes() < HEADER_SIZE) {
			return;
		}
		int mark = in.readerIndex();//read the position
		// get开头的方法读取字节时不会移动指针,read开头的方法读取字节时的指针会移动
		byte type = in.readByte();// 消息类型
		int length = in.readInt();// 消息长度

		int dl = length - HEADER_SIZE;// 消息内容长度
		if (in.readableBytes() < dl) {
			in.readerIndex(mark);
			return;
		}
		ByteBuf buf = in.readBytes(dl);// 消息内容
		String data = buf.toString(Charset.forName("UTF-8"));
		out.add(new HeartbeatMessage(type, data));
	}

}
