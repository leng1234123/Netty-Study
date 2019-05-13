package com.netty.protstack.message;

/**
 * 协议栈
 * @author lengyul
 * @date 2019年3月19日 下午9:09:15
 */
public class NettyMessage {
	
	private Header header; // 消息头
	
	private Object body; // 消息体

	public NettyMessage() {}
	
	public NettyMessage(Header header, Object body) {
		this.header = header;
		this.body = body;
	}
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "NettyMessage [header=" + header.toString() + ", body=" + body.toString() + "]";
	}
	
	
}
