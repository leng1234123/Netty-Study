package com.netty.heartbeat.message;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 服务端消息入站处理
 * @author lengyul
 *
 */
public class MessageDecoder extends LengthFieldBasedFrameDecoder  {

    //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是byte+int = 1+4 = 5
    private final int HEADER_SIZE = 5;
	private int count = 0;
    /**
     * 
     * @param maxFrameLength  解码时，处理每个帧数据的最大长度
     * @param lengthFieldOffset 该帧数据中，存放该帧数据的长度的数据的起始位置
     * @param lengthFieldLength 记录该帧数据长度的字段本身的长度
     * @param lengthAdjustment  修改帧数据长度字段中定义的值，可以为负数
     * @param initialBytesToStrip 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     * @param failFast
     */
	public MessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
			super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
	}
	
	 @Override
	 protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		 if (in == null) {
			return null;
		 }
		 
		 System.out.println(this.getClass().hashCode()+"\t readableBytes - "+in.readableBytes() + "\t" + "count - " + ++count);
		 int mark = in.readerIndex();//read the position
		 //如果可读字节数小于头部信息
		 if (in.readableBytes() < HEADER_SIZE) {
			// throw new Exception("可读字节数小于头部信息,数据格式不正确");
			 return null;
		 }
		 //get开头的方法读取字节时不会移动指针,read开头的方法读取字节时的指针会移动
		 byte type = in.readByte();//消息类型
		 int length = in.readInt();//消息长度
		 
		 int dl = length - HEADER_SIZE;//消息内容长度
		 if (in.readableBytes() < dl) {
			// throw new Exception("可读字节数小于数据长度,数据格式不正确");
			 in.readerIndex(mark);
			 return null;
		 }
		 ByteBuf buf = in.readBytes(dl);//消息内容
		 String data = buf.toString(Charset.forName("UTF-8"));
		 return new HeartbeatMessage(type, data);
	 }

	

}
