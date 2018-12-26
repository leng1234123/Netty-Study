package com.netty.protobuf.client;

import com.netty.protobuf.MsgReqProto;
import com.netty.protobuf.MsgReqProto.MsgReq;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMsgHandler extends SimpleChannelInboundHandler<MsgReq>{
	
	/**
	 * 建立连接后该 channelActive() 方法被调用一次
	 */
	 @Override
	 public void channelActive(ChannelHandlerContext ctx) {
	       /* ctx.writeAndFlush(Unpooled.copiedBuffer("A1Hello,World!", 
	        CharsetUtil.UTF_8));*/
		 	for (int i = 0; i < 5; i++) {
		 		MsgReqProto.MsgReq msg = MsgReqProto.MsgReq.newBuilder().setId(i).setData("asdasasdasdasqweqw").build();
				ctx.writeAndFlush(msg);
			}
	 }
	
	 /**
	  * 接收到数据时被调用
	  */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MsgReq msg) throws Exception {

		 System.out.println("Client received: " + msg.toString());
		
	}

}
