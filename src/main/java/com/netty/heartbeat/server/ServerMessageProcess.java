package com.netty.heartbeat.server;

import com.netty.heartbeat.message.HeartbeatMessage;

import io.netty.channel.ChannelHandlerContext;

public class ServerMessageProcess {
	
	 public void messageProcess(ChannelHandlerContext ctx,HeartbeatMessage msg){
		  	
		 byte type = msg.getType();
		 switch (type) {
		 case (byte) 0xA1:
			 System.out.println("心跳指令"+ctx.channel().remoteAddress() + "->Server :" + msg.toString());  
			break;
		 case (byte) 0xA2:
			 System.out.println("数据指令"+ msg.toString());	
				break;
		 case (byte) 0xA3:
			 System.out.println("A3");		
				break;
		 default:
			 	break;
		}
	 }
	
}
