package com.netty.redis.msg;

import java.util.Map;

/**
 * RESP发送数据协议Msg(REdis Serialization Protocol)
 * @author lengyul
 *	<参数数量> CR LF
	$<参数 1 的字节数量> CR LF
	<参数 1 的数据> CR LF...
	$<参数 N 的字节数量> CR LF
	<参数 N 的数据> CR LF
 */
public class RedisMsg {
	
	private RedisCmd cmd; 
	
	private Map<String,Object> dataMap;
	
	public final static String CRLF = "\r\n";

	public RedisMsg(){}
	
	public RedisMsg(RedisCmd cmd) {
		this.cmd = cmd;
	}
	
	public RedisMsg(RedisCmd cmd, Map<String, Object> dataMap) {
		this.cmd = cmd;
		this.dataMap = dataMap;
	}

	public RedisCmd getCmd() {
		return cmd;
	}

	public void setCmd(RedisCmd cmd) {
		this.cmd = cmd;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	
	
	
}
