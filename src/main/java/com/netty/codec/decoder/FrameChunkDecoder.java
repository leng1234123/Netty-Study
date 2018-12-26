package com.netty.codec.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 设置可读字节数最大值
 * @author lengyul
 * @date 2018年12月5日 下午3:41:29
 */
public class FrameChunkDecoder extends ByteToMessageDecoder {
	
	 private final int maxFrameSize;

	 public FrameChunkDecoder(int maxFrameSize) {
	        this.maxFrameSize = maxFrameSize;
	 }
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		 int readableBytes = in.readableBytes();  //ByteBuf可读字节数
		 if (readableBytes > maxFrameSize)  {
	            //可读字节大于自定义最大字节数，清空这个ByteBuf
	            in.clear();
	            throw new TooLongFrameException();
	        }
		 	//读取可读字节数
	        byte [] bytes = new byte[readableBytes];
	        in.readBytes(bytes);
		 	//ByteBuf buf = in.readBytes(readableBytes); 
	        out.add(bytes); 
	}
	

}
