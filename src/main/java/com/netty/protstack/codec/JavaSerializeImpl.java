package com.netty.protstack.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.netty.protstack.message.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Java I/O实现序列化 反序列化
 * 
 * @author lengyul
 * @date 2019年3月20日 上午9:37:40
 */
public class JavaSerializeImpl {

	/**
	 * 将对象序列化为字节并写入ByteBuf
	 * 对象需要实现序列化接口
	 * @param object
	 * @param out
	 */
	public static void encode(Object msg, ByteBuf out) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(msg);
			oos.flush();
			// 获取字节数组，写入缓冲区
			byte[] bytes = bos.toByteArray();
			out.writeInt(bytes.length);
			out.writeBytes(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 将字节反序列化为对象
	 * @param bytes
	 * @return
	 */
	public static Object decode(byte[] bytes) {
		Object object = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(bis);
			object = ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return object;
	}

	
	public static void main(String[] args) {
		
		ByteBuf out = Unpooled.directBuffer(); // 直接缓冲区
		/* out.writeInt(100);
		/*	System.out.println("out length is " + out.readableBytes());*/
		UserInfo userInfo = new UserInfo("lengyul", 100001);
		encode(userInfo, out);
		System.out.println("[Serializable] out length is " + out.readableBytes());
		System.out.println(out.getInt(0)); //从0开始读4个字节 对象序列化后的长度
		int length = out.readInt();
		/* 使用直接缓冲区调用array()方法返回字节数组会出现  
		java.lang.UnsupportedOperationException: direct buffer
		由于是直接内存，因此无法直接转换成堆内存，因此它并不支持array()方法。
		*/
		ByteBuf dst = Unpooled.copiedBuffer(out.readBytes(length));
		Object object = decode(dst.array());
		System.out.println(object);
	}
	
}
