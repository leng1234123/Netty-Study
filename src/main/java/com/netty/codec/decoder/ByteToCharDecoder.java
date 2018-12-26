package com.netty.codec.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 字节转换为字符
 * @author lengyul
 * @date 2018年12月5日 下午3:48:22
 */
public class ByteToCharDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= 2) {  //java中char类型占2个字节
            out.add(in.readChar());
        }
	}

}
