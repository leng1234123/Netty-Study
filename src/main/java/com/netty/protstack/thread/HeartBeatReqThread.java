package com.netty.protstack.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import com.netty.protstack.message.BuildMessage;
import com.netty.protstack.message.MessageType;
import com.netty.protstack.message.NettyMessage;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端心跳请求线程
 * @author lengyul
 * @date 2019年3月22日 上午10:46:27
 */
public class HeartBeatReqThread implements Runnable {

	private int rate = 0; // 发送心跳次数
	private int discon = 0; //断开次数
	private TimeUnit timeUnit = null; // 心跳频率
	private ChannelHandlerContext ctx = null;
	private volatile long IdleTime = 30; //空闲时间
	private volatile boolean status = false; // 状态
	
	public HeartBeatReqThread() {} 
	
	public HeartBeatReqThread(ChannelHandlerContext ctx , long IdleTime ,TimeUnit timeUnit) {
		this.ctx = ctx;
		this.IdleTime = IdleTime;
		this.timeUnit = timeUnit;
	}
	
	
	@Override
	public void run() {
		while (status && !Thread.currentThread().isInterrupted()) {
			try {
				timeUnit.sleep(IdleTime);
				NettyMessage message = BuildMessage.buildMessage(MessageType.HB_REQ, MessageType.BODY_OK);
				rate++;
				System.out.println("["+LocalDateTime.now()+"]" + ctx.channel().remoteAddress() + " 发送心跳次数：" + rate + "，断连次数：" + discon);
				ctx.writeAndFlush(message);	
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		discon++;
		System.out.println("["+LocalDateTime.now()+"]"+ctx.channel().remoteAddress()+" 心跳已停止");
	}
	
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	public long getIdleTime() {
		return IdleTime;
	}

	public void setIdleTime(long idleTime) {
		IdleTime = idleTime;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}
	
	public int getDiscon() {
		return discon;
	}
	
	
}
