package com.netty.protstack.codec;

import java.util.HashMap;
import java.util.Map;

import com.netty.protstack.message.Header;
import com.netty.protstack.message.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

	public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
	}
	
	@Override
	protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		/*ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}*/
		
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		
		header.setCrcCode(in.readInt());
		header.setLength(in.readInt());
		header.setSessionID(in.readLong());
		header.setType(in.readByte());
		header.setPriority(in.readByte());
		
		int size = in.readInt(); // attachment size
		if (size > 0) {
			int keySize = 0; 
			byte[] keyArray = null;
			String key = null;
			int valSize = 0;
			byte[] valArray = null;
			Object val = null;
			Map<String, Object> attch = new HashMap<>(size);
			for (int i = 0; i < size; i++) {
				// 获取key
				keySize = in.readInt();
				keyArray = new byte[keySize];
				in.readBytes(keyArray);
				key = new String(keyArray, "UTF-8");
				// 获取value
				valSize = in.readInt();
				valArray = new byte[valSize];
				val = MessagePackCodec.decode(valArray);
				attch.put(key, val);
			}
			header.setAttachment(attch);
		}
		
		if (in.readableBytes() >= 4) {
			int blen = in.readInt();
			if (blen > 0 && blen == in.readableBytes()) {				
				message.setBody(MessagePackCodec.decode(in));
			}
		}
		
		message.setHeader(header);
		return message;
	}

}
