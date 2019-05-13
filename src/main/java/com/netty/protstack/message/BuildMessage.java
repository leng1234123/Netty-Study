package com.netty.protstack.message;

public class BuildMessage {
	
	
	public static NettyMessage buildMessage(byte type,Object body) {
		Header header =  new Header(type);
		return new NettyMessage(header,body);
	} 
	
	public static NettyMessage buildLoginReq() {
		Header header = new Header(MessageType.LOGIN_REQ);
		return new NettyMessage(header,null);
	}
	
	public static NettyMessage buildResponse(byte result) {
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP);
		return new NettyMessage(header, result);
	}
	
}
