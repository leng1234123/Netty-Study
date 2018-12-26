package com.netty.udp.send;

import java.net.InetSocketAddress;

/**
 * 日志消息文件
 * @author lengyul
 *
 */
public final class LogEvent {
	   
		public static final byte SEPARATOR = (byte)':';
		
		private final InetSocketAddress source; //返回发送 LogEvent 的 InetSocketAddress 的资源
		private final String logfile;	//返回用于发送 LogEvent 的日志文件的名称
		private final String msg;		//返回消息的内容
		private final long received;	//返回 LogEvent 接收到的时间
		
		public LogEvent(String logfile,String msg){ //构造器用于出站消息
			this(null,-1,logfile,msg);
		}
		
		public LogEvent(InetSocketAddress source, long received, String logfile, String msg) { //构造器用于入站消息
	        this.source = source;
	        this.logfile = logfile;
	        this.msg = msg;
	        this.received = received;
	    }

		public InetSocketAddress getSource() {
			return source;
		}

		public String getLogfile() {
			return logfile;
		}

		public String getMsg() {
			return msg;
		}

		public long getReceived() {
			return received;
		}
		
		
}
