package com.netty.spdy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.eclipse.jetty.npn.NextProtoNego;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;

public class SpdyChannelInitializer extends ChannelInitializer<Channel> {
	
	private final SSLContext context;
	 
	public SpdyChannelInitializer(SSLContext context) {
		this.context = context;
	}

	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline  = ch.pipeline();
		SSLEngine engine = context.createSSLEngine();
		engine.setUseClientMode(false);
		NextProtoNego.put(engine, new DefaultServerProvider());
		NextProtoNego.debug = true;
		pipeline.addLast("sslHandler", new SslHandler(engine));
		/*pipeline.addLast("chooser",
				new DefaultSpdyOrHttpChooser());*/
		
	}

}
