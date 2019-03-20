package com.netty.buffer;

import java.util.Iterator;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Netty 缓冲区之ByteBuf
 * @author lengyul
 * @date 2019年3月20日 下午3:12:01
 */
public class NettyByteBuf {
	/*
	 * ByteBuf的三种类型
	 * Heap Buffer（堆缓冲区）
	 * 数据存储在JVM的堆中，将数据存储在数组的实现。通过ByteBuf.array()方法获取字节数组。
	 * 如果堆缓冲区去访问直接缓冲区的字节数组会出现异常->java.lang.UnsupportedOperationException: direct buffer
	 * 
	 * Direct Buffer（直接缓冲区）
	 * 在堆外直接分配内存，不会占用堆内存空间，零拷贝，传输速度快，
	 * 但是分配内存和释放内存时比堆缓冲区复杂，如果出现堆外内存溢出，难以排查。
	 * 
	 * Composite Buffer（复合缓冲区）
	 * 可以将多个不同的ByteBuf，合并到组合缓冲区中（零拷贝），然后提供这些ByteBuf的组合视图。
	 */
	
	// 三种缓冲区创建方式
	@Test
	public void buffer() {
		ByteBuf buf = Unpooled.buffer(5); // 堆缓冲区		
		ByteBuf directBuf = Unpooled.directBuffer(10); // 直接缓冲区
		CompositeByteBuf compBuf = Unpooled.compositeBuffer(); //复合缓冲区
		compBuf.addComponents(true,buf,directBuf); // true表示添加ByteBuf时，自动改变writeIndex 添加多个缓冲区
		//迭代
		/*Iterator<ByteBuf> iter = compBuf.iterator();
		 while(iter.hasNext()){
		   System.out.println(iter.next().toString());  
		 }
		System.out.println(compBuf.readableBytes()); // 无可读字节 */		
	}
	 
	// ByteBuf API
	@Test
	public void byteBuf() {
		ByteBuf buf = Unpooled.buffer(18);
		System.out.println(buf.readableBytes()); // 可读字节 0
		System.out.println(buf.writableBytes()); // 可写字节 18
		System.out.println(buf.readerIndex()); // 读索引 0
		System.out.println(buf.writerIndex()); // 写索引 0
		System.out.println(buf.capacity()); // 总容量 18
		
		//读写操作
		buf.setByte(0, 0x00); // 在指定的索引位置设置 byte值，当前写索引为 0 ，没有字节，无效
		buf.writeByte(0x01); // 写索引 + 1
		System.out.println("可读字节：" + buf.readableBytes());
		buf.getByte(0); // get方法获取数据时不会改变缓冲区读索引
		buf.readByte(); // read方法读取数据时会改变缓冲区的读索引
		
		//索引管理
		buf.markReaderIndex();buf.markWriterIndex(); // 标记读/写索引位置
		buf.resetReaderIndex();buf.resetWriterIndex(); // 读/写索引回到mark标记的索引值
	//	buf.readerIndex(readerIndex);buf.writerIndex(writerIndex); // 读/写索引设置到指定位置
		buf.clear(); //readerIndex 和 writerIndex = 0，但是不会清除内存当中的内容
		
		//查找
	//	buf.forEachByte(processor);
		
	}
	
	// 零拷贝操作
	@Test
	public void zeroCopy() {
		
		byte[] bytes = new byte[10];
		//传统方式
		ByteBuf byteBuf = Unpooled.buffer();
		byteBuf.writeBytes(bytes); // 拷贝操作
		
		// 包装这个byte数组，生成一个新的ByteBuf实例，不需要进行拷贝操作
		ByteBuf byteBuf1 = Unpooled.wrappedBuffer(bytes);
		
		//slice 操作实现零拷贝
		byteBuf1.slice();
		byteBuf1.slice(0, 5); //指定索引，长度
	}
	
	
	}
