package com.netty.protstack.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端心跳线程执行器
 * @author lengyul
 * @date 2019年3月23日 下午2:02:04
 */
public class HeartBeatThreadExecutor {
	
	private static ScheduledExecutorService hbtExecutor = Executors.newScheduledThreadPool(1);
	
	private static HeartBeatReqThread heartBeatReqThread = new HeartBeatReqThread();
	
	public static boolean isShutdown() {
		return hbtExecutor.isShutdown();
	}
	
	
	public static void setCtx(ChannelHandlerContext ctx) {
		heartBeatReqThread.setCtx(ctx);
	}
	
	public static void execute() {
		if (heartBeatReqThread.getCtx() != null) {			
			execute(heartBeatReqThread.getCtx(), 8, TimeUnit.SECONDS);
		}
	}
	
	public static void execute(ChannelHandlerContext ctx,long idleTime, TimeUnit timeUnit) {
	//	heartBeatReqThread = new HeartBeatReqThread(ctx, IdleTime, TimeUnit.SECONDS);
		heartBeatReqThread.setCtx(ctx);
		heartBeatReqThread.setIdleTime(idleTime);
		heartBeatReqThread.setTimeUnit(timeUnit);
		heartBeatReqThread.setStatus(true);
		System.out.println("心跳信息配置成功！");
		hbtExecutor.execute(heartBeatReqThread);
	}

	public static void setStatus(boolean status) {
		heartBeatReqThread.setStatus(status);
	}
	
	
}
