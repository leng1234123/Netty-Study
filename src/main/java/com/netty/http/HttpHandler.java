package com.netty.http;

import java.util.Scanner;

import com.netty.utils.HttpHandleUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AsciiString;

/**
 * HTTP服务器的业务处理
 * 处理消息是FullHttpRequest的Channel
 * @author lengyul
 *
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

	 private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		System.out.println("class:" + msg.getClass().getName());
		
		//将接收到数据转换为Map形式
    	HttpHandleUtils.getParamsFromChannel(ctx, msg);
    	
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("test".getBytes()));
                //output()); 
				
		HttpHeaders heads = response.headers();
		heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());//描述数据长度
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.write(response); //输出缓冲流,将所接收的消息返回给发送者
	}
	
	/**
	 * 手动输入发送信息
	 * @return
	 */
	public ByteBuf output(){
		   Scanner sc = new Scanner(System.in);
		   System.out.print("输入发送信息:");
		   String str = sc.nextLine();
		   ByteBuf buf = Unpooled.wrappedBuffer(str.getBytes());
		   sc.close();
		   return buf;
	} 
	
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        super.channelReadComplete(ctx);
        ctx.flush();  
    }

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught");
        if(null != cause) cause.printStackTrace();
        if(null != ctx) ctx.close();
    }


}
