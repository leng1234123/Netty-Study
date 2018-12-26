package com.netty.heartbeat.message;

import io.netty.channel.ChannelHandlerContext;

public class ServerMessageProcess {
	
	 public void messageProcess(ChannelHandlerContext ctx,HeartbeatMsg msg){
		  	
		 byte type = msg.getType();
		 switch (type) {
		 case (byte) 0xA1:
			 System.out.println("心跳指令"+ctx.channel().remoteAddress() + "->Server :" + msg.toString());  
			break;
		 case (byte) 0xA2:
			 System.out.println("A2");	
				break;
		 case (byte) 0xA3:
			 System.out.println("A3");		
				break;
		 default:
			 	break;
		}
	 }
	
}
