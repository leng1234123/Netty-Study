package com.netty.heartbeat.message;

/**
 * 发送数据协议 Msg
 * @author lengyul
 *
 */
public class HeartbeatMsg {
	
	private byte type; //类型
	
	private byte flag;//信息标志
	
	private int length;//数据长度
	
	private String data;//发送数据

	public HeartbeatMsg(){}
	
	public HeartbeatMsg(byte type, byte flag, int length, String data) {
		super();
		this.type = type;
		this.flag = flag;
		this.length = length;
		this.data = data;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "HeartbeatMsg [type=" + type + ", flag=" + flag + ", length=" + length + ", data=" + data + "]";
	}
	
	
}
