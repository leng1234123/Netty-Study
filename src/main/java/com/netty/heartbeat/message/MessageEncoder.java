package com.netty.heartbeat.message;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * 客户端消息出站处理
 * @author lengyul
 *
 */
public class MessageEncoder extends MessageToByteEncoder<HeartbeatMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HeartbeatMessage msg, ByteBuf out) throws Exception {
		if (null == msg) {
			throw new NullPointerException("msg is null");
		}
		out.writeByte(msg.getType());
		byte[] dataBytes = msg.getData().getBytes(CharsetUtil.UTF_8);
		out.writeInt(msg.getLength());
		out.writeBytes(dataBytes);
		System.out.println("client1: "+msg.toString());
	}

}
