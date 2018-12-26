package com.netty.unit;

import org.junit.Test;

import com.netty.codec.decoder.FixedLengthFrameDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import junit.framework.Assert;

public class FixedLengthFrameDecoderTest {
		
	@Test
	public void testFramesDecoded() {
		ByteBuf buf = Unpooled.buffer(); //2
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FixedLengthFrameDecoder(3)); //3
        Assert.assertFalse(channel.writeInbound(input.readBytes(2))); //4
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));
		
        Assert.assertTrue(channel.finish());  //5
        ByteBuf read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();
        
        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        read = (ByteBuf) channel.readInbound();
        Assert.assertEquals(buf.readSlice(3), read);
        read.release();

        Assert.assertNull(channel.readInbound());
        buf.release();
	}
	
}
