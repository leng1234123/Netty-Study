package com.netty.protstack.message;

import java.util.HashMap;
import java.util.Map;

/**
 * Netty协议栈 消息头
 * @author lengyul
 * @date 2019年3月19日 下午9:03:22
 */
public final class Header {

	private int crcCode = 0xAABB0101; //效验码 三部分（固定值+主版本号，此版本号）
	
	private int length; // 消息长度（消息头+消息体）
	
	private long sessionID = System.currentTimeMillis(); // 会话ID
	
	private byte type; // 消息类型
	
	private byte priority = MessageType.MSG_LEVEL; // 消息优先级 0 - 255
	
	private Map<String, Object> attachment = new HashMap<>(); // 扩展消息头

	public Header() {}
	
	public Header(byte type) {
		this.type = type;
	}
	
	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type
				+ ", priority=" + priority + ", attachment=" + attachment + "]";
	}

	
	
}
