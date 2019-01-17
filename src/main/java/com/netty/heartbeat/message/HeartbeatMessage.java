package com.netty.heartbeat.message;

/**
 * 发送数据协议 Msg
 * @author lengyul
 *
 */
public class HeartbeatMessage {
	
	private int length;//消息长度
	
	private byte type;//消息标记
		
	private String data;//消息内容

	public HeartbeatMessage(){}
	
	public HeartbeatMessage(byte type, String data) {
		this.type = type;
		this.data = data;
		this.length = 4 + 1 + data.getBytes().length;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HeartbeatMessage [length=" + length + ", type=" + type + ", data=" + data + "]";
	}

	
	
	
}
