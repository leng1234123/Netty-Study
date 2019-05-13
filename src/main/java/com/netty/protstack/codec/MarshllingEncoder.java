package com.netty.protstack.codec;

import org.jboss.marshalling.Marshaller;

import io.netty.buffer.ByteBuf;

/**
 * Marshlling 对象序列化
 * @author lengyul
 * @date 2019年3月19日 下午9:25:25
 */
public class MarshllingEncoder {
	
	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	
	Marshaller marshaller;
	
	public MarshllingEncoder() {
		
	//	marshaller = Mar
	}
	
	protected void encode(Object msg, ByteBuf out) {
		
		int lengthPos = out.writableBytes();
		out.writeBytes(LENGTH_PLACEHOLDER);
	//	Channelbufferby
		
	}
	
}
