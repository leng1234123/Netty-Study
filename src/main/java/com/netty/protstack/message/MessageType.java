package com.netty.protstack.message;

public class MessageType {
	
	public static final byte BODY_OK = 0x00; // 成功
	
	public static final byte BODY_FAIL = 0xFFFFFFFF; // 错误
	
	public static final byte SER_REQ = 0x01; //业务请求
	
	public static final byte SER_RESP = 0x02; // 业务响应
	
	public static final byte LOGIN_REQ = 0x03;// 登陆请求

	public static final byte LOGIN_RESP = 0x04; // 登录响应
	
	public static final byte HB_REQ = 0x05; // 心跳请求
	
	public static final byte HB_RESP = 0x06; // 心跳响应
	
	public static final byte MSG_LEVEL = 0x10; // 消息级别
	
}
