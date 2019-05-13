package com.netty.protstack.codec;

import java.util.List;
import java.util.Map;

import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

public class NettyMessgaeEncoder extends MessageToMessageEncoder<NettyMessage> {

	/*private MarshllingEncoder marshllingEncoder;
	
	public NettyMessgaeEncoder() {
		this.marshllingEncoder = new MarshllingEncoder();
	}*/
	
	@Override
	protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
		if (msg == null || msg.getHeader() == null) {
			throw new NullPointerException("The encode message is null");
		}
		ByteBuf sendBuf = Unpooled.buffer(); // 堆缓冲区 自动扩容 
		
		sendBuf.writeInt(msg.getHeader().getCrcCode());
		sendBuf.writeInt(msg.getHeader().getLength());
		sendBuf.writeLong(msg.getHeader().getSessionID());
		sendBuf.writeByte(msg.getHeader().getType());
		sendBuf.writeByte(msg.getHeader().getPriority());
		
		Map<String, Object> att = msg.getHeader().getAttachment();
		sendBuf.writeInt(att.size());
		
		String key = null;
		byte[] keyArray = null;
		Object value = null;
		for (Map.Entry<String, Object> param : att.entrySet()) {
			key = param.getKey();
			keyArray = key.getBytes("UTF-8");
			sendBuf.writeInt(keyArray.length);
			sendBuf.writeBytes(keyArray);
			value = param.getValue(); 
			MessagePackCodec.encode(value, sendBuf);
		}
		key = null;
		keyArray = null;
		value = null;
		if (msg.getBody() != null) {
			MessagePackCodec.encode(msg.getBody(), sendBuf); // 22 
		} else {
			sendBuf.writeInt(0); //可选参数
		}		
		sendBuf.setInt(4, sendBuf.readableBytes()); // 指定索引位置处（length字段）设置总长度
		out.add(sendBuf);
	}

}
