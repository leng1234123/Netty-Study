package com.netty.udp.accpet;

import java.util.List;

import com.netty.udp.send.LogEvent;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

/**
 * 消息转换器
 * @author lengyul
 *
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {

	@Override
	protected void decode(ChannelHandlerContext ctx, DatagramPacket datagramPacket, List<Object> out) throws Exception {
		ByteBuf data = datagramPacket.content();
		int i = data.indexOf(0, data.readableBytes(), LogEvent.SEPARATOR); //获取":"位置
		String filename = data.slice(0, i).toString(CharsetUtil.UTF_8);//从数据中读取文件名
		String logMsg =  data.slice(i + 1, data.readableBytes()).toString(CharsetUtil.UTF_8);//读取数据中的日志消息
		//构造新的 LogEvent 对象并将其添加到列表中
		LogEvent event = new LogEvent(datagramPacket.recipient(),System.currentTimeMillis(),filename,logMsg);
		out.add(event);
	}

}
