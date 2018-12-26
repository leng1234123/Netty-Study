package com.netty.codec.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 足够的数据可以读取时将产生固定大小的包
 * @author lengyul
 *
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
	
	private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) { //设置每次接收数字节大小
        if (frameLength <= 0) {
            throw new IllegalArgumentException(
                    "frameLength must be a positive integer: " + frameLength);
        }
        this.frameLength = frameLength;
    }
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= frameLength) { //如果可读字节数大于设置字节数，进行读取
            ByteBuf buf = in.readBytes(frameLength);
            out.add(buf);
        }
	}

}
