package com.netty.utils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * Http响应业务处理类
 * @author lengyul
 *
 */
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		//如果响应是预期的写入
		if (HttpHeaders.is100ContinueExpected(request)) {
            send100Continue(ctx); //2
        }
		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK); //3
		response.content().writeBytes(getContent().getBytes(CharsetUtil.UTF_8));//设置写入内容
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");//设置头文件
		
		boolean keepAlive = HttpHeaders.isKeepAlive(request); //检查请求设置是否启用了 keepalive
		if (keepAlive) { 
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
		ChannelFuture future = ctx.writeAndFlush(response);
		if (!keepAlive) { //如果响应不是 keepalive，在写完成时关闭连接
            future.addListener (ChannelFutureListener.CLOSE); 
        }
		
	}
	
	protected String getContent() {  
        return "This content is transmitted via HTTP\r\n";
    }
	
	/**
	 * Helper方法生成了100 持续的响应，并写回给客户端
	 * @param ctx
	 */
	private static void send100Continue(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }
	
	/**
	 * 若执行阶段抛出异常，则关闭管道
	 */
	 @Override
	 public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	        throws Exception {  
	        cause.printStackTrace();
	        ctx.close();
	    }

}
