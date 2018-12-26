package com.netty.udp.send;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

	private final InetSocketAddress remoteAddress;
	
	public LogEventEncoder(InetSocketAddress remoteAddress) {  
        this.remoteAddress = remoteAddress;
    }
	
	@Override
	protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
		byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
		byte[] msg  = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
		ByteBuf buf = ctx.alloc().buffer(file.length + msg.length + 1);
		buf.writeBytes(file);//写入文件
		buf.writeByte(LogEvent.SEPARATOR);//分隔符 ":"
		buf.writeBytes(msg);//写入消息
		
		out.add(new DatagramPacket(buf, remoteAddress));//添加新的 DatagramPacket 到出站消息
	}

}
