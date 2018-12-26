package com.netty.protobuf.server;

import com.netty.protobuf.MsgReqProto;
import com.netty.protobuf.MsgReqProto.MsgReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ServerMsgHandler extends SimpleChannelInboundHandler<MsgReq>{
		

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MsgReq msg) throws Exception {
		
		System.out.println("\n接收到客户端"+ctx.channel()+"发送数据为:"+msg.toString());
		
	}
	

}
