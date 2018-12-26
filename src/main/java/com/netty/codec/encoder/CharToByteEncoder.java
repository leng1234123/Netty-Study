package com.netty.codec.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 字符转字节
 * @author lengyul
 * @date 2018年12月5日 下午3:50:03
 */
public class CharToByteEncoder extends MessageToByteEncoder<Character> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Character msg, ByteBuf out) throws Exception {
		out.writeChar(msg);
	}

}
