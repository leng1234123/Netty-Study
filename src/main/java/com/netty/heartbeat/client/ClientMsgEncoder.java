package com.netty.heartbeat.client;

import java.nio.charset.Charset;

import com.netty.heartbeat.message.HeartbeatMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * 客户端消息出站处理
 * @author lengyul
 *
 */
public class ClientMsgEncoder extends MessageToByteEncoder<HeartbeatMsg> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HeartbeatMsg msg, ByteBuf out) throws Exception {
		if (null == msg) {
			throw new NullPointerException("msg is null");
		}
		
		String  data = msg.getData();
		byte[] dataBytes = data.getBytes(CharsetUtil.UTF_8);
		
		out.writeByte(msg.getType());
		out.writeByte(msg.getFlag());
		out.writeInt(dataBytes.length);
		out.writeBytes(dataBytes);
	}

}
