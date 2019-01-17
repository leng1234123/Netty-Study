package com.netty.codec.codes;

import com.netty.codec.decoder.ByteToCharDecoder;
import com.netty.codec.encoder.CharToByteEncoder;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 处理进站字节和出站消息
 * @author lengyul
 *
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
	public CombinedByteCharCodec() {
		super(new ByteToCharDecoder(), new CharToByteEncoder());
	}
}
