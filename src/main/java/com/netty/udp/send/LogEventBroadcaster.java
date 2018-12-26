package com.netty.udp.send;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class LogEventBroadcaster {
		
	   private final Bootstrap bootstrap;
	   private final File file;
	   private final EventLoopGroup group;
	   
	   public LogEventBroadcaster(InetSocketAddress address,File file){
		   group = new NioEventLoopGroup();
		   bootstrap = new Bootstrap();
		   bootstrap.group(group)
		   			.channel(NioDatagramChannel.class)
		   			.option(ChannelOption.SO_BROADCAST,true)
		   			.handler(new LogEventEncoder(address));
	   
		   this.file = file;
	   }
	   
	   public void run() throws IOException{
		   Channel ch = bootstrap.bind(0).syncUninterruptibly().channel();
		   System.out.println("LogEventBroadcaster running");
		   long pointer = 0;
		   for (;;) {
			   long len = file.length();
			   if (len < pointer) {
				   pointer = len; //3
			   }else if(len > pointer){
				   RandomAccessFile raf =new RandomAccessFile(file,"r");
				   raf.seek(pointer);
				   String line;
				   while ((line = raf.readLine()) != null) {
					      ch.writeAndFlush(new LogEvent(null,-1,file.getAbsolutePath(),line));
				   }
				   pointer = raf.getFilePointer();
				   raf.close();
			   }
			   try {
	               Thread.sleep(1000);  //7
	           } catch (InterruptedException e) {
	               Thread.interrupted();
	               break;
	           }
		   }   
	   }
	   
	   public void stop() {
	        group.shutdownGracefully();
	   }
	   
	   
	   
	   public static void main(String[] args) throws IOException {
		   /*	if (args.length != 2) {
	            throw new IllegalArgumentException();
	        }*/
		   
		   LogEventBroadcaster broadcaster = new LogEventBroadcaster(new InetSocketAddress("255.255.255.255",
	                8888), new File("E:\\logtest.txt"));
		   
		   try {
			   broadcaster.run();
		   }finally{
			   broadcaster.stop();
		   }
	   }
	   
}
