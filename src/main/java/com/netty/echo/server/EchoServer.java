package com.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {
	   
	private final int port;

    public EchoServer(int port) {
        this.port = port;
    }
    
    public static void main(String[] args) throws InterruptedException {
		
    	new EchoServer(9908).start();
    	
	}
    /* 创建Channel
     * 1. bind() -> doBind() -> initAndRegister() 
     * -> newChannel()[NioServerSocketChannel.class][parent,id,unsafe,pipeline]
           *    初始化Channel
     * 2.init(channel)[options,attrs,childGroup,childHandler]
     * -> [ServerBootstrapAcceptor.class] 连接处理器
     * 	注册Selector
     * 3.register(channel) 将channel注册到Selector -> invokeHandlerAddedIfNeeded() 
     * -> fireChannelRegistered() 
           *     端口绑定，注册事件
     * 4.doBind() -> fireChannelActive() -> beginRead() 绑定完成 注册事件OP_ACCPET
     */
    public void start() throws InterruptedException {
    	EventLoopGroup bossGroup = new NioEventLoopGroup(1); //负责处理客户端连接线程
    	EventLoopGroup workerGroup = new NioEventLoopGroup();//读写线程默认CPU处理器数量 * 2
    	/*
    	 *  创建NioEventLoopGroup()  
    	 *  Executor(线程执行器) 使用DefaultThreadFactory类中的newThread()对线程进行封装并设置线程名称 --> 
    	 *  EventExecutor(保存创建的NioEventLoop)  NioEventLoop调用父类构造器配置参数  -->
    	 *  EventExecutorChooser(负责给新连接绑定对应的NioEventLoop)，next()方法返回一个EventExecutor[i] = NioEventLoop
    	 */
    	
    	try {
			ServerBootstrap bootstrap = new ServerBootstrap();//创建 ServerBootstrap 辅助类
			bootstrap
			// .group(bossGroup) //单线程处理 --> Reactor单线程模型
			.group(bossGroup, workerGroup)//多线程处理 --> Reactor多线程模型
			.channel(NioServerSocketChannel.class) //extends Channel，指定使用 NIO的传输Channel，调用bind方法时会进行初始化
			.localAddress(port) //设置 socket 地址使用所选的端口
			//extends ChannelHandler，添 handlers到 Channel中的的ChannelPipeline
			 .handler(new EchoServerHandler())
			.childHandler(new ChannelInitializer<SocketChannel>() { 
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
							/* ch.pipeline().addLast("handler",new EchoServerHandler()); */
				}
			});
			ChannelFuture f = bootstrap.bind().sync(); //绑定的服务器;sync 等待服务器关闭
		//	System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
			f.channel().closeFuture().sync();//关闭 channel 和 块，直到它被关闭
    	} catch (Exception e) {
			e.printStackTrace();
		} finally{
			bossGroup.shutdownGracefully().sync();//关闭 EventLoopGroup，释放所有资源
			workerGroup.shutdownGracefully().sync();
		}
    }
    
    
}
