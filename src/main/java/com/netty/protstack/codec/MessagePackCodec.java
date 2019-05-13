package com.netty.protstack.codec;

import java.io.IOException;
import java.util.Objects;

import org.msgpack.MessagePack;
import org.msgpack.template.Template;

import com.netty.protstack.message.UserInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * MessagePack 对象序列化/反序列化
 * @author lengyul
 * @date 2019年3月20日 上午10:36:57
 * Core library of the MessagePack for Java
 * 使用：需要对序列化的类加 @Message 注解，或者MessagePack中的register方法指定Class 
 */
public class MessagePackCodec {

	public static <T> void encode(Object msg, ByteBuf out) {
		MessagePack msgpack = new MessagePack();
		try {
			if (!isPrimitive(msg)) { //为false时为引用类型				
				msgpack.register(msg.getClass());	// 基本数据类型可能会出错			
			}
			byte[] bytes = msgpack.write(msg);
			out.writeInt(bytes.length);
			out.writeBytes(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断Object对象为基本类型 or 引用类型
	 * @param obj
	 * @return
	 */
	private static boolean isPrimitive(Object obj) {
		try {
			return ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive();
		} catch (Exception e) {
			return false;
		}
	}
	
	public static Object decode(byte[] bytes) {
		return  decode(bytes, null);
	}
	
	public static Object decode(ByteBuf byteBuf) {
		byte[] bytes = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		return decode(bytes);
	}
	
	/**
	 * 将字节数组反序列化为指定类对象
	 * @param bytes
	 * @param clazz
	 * @return
	 */
	public static Object decode(byte[] bytes, Class<?> clazz) {
		MessagePack msgpack = new MessagePack();
		Object msg = null;
		try {
		if (Objects.isNull(clazz)) {			
			msg = msgpack.read(bytes);
		} else {			
			msgpack.register(clazz);
			msg = msgpack.read(bytes, clazz);
		}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 根据模板序列化对象（数据类型）
	 * @param bytes
	 * @param tpl
	 * @return
	 */
	public static <T> Object decodeTpl(byte[] bytes, Template<T> tpl) {
		MessagePack msgpack = new MessagePack();
		T msg = null;
		try {
			msg = msgpack.read(bytes, tpl); // 使用模板类型
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return msg;
	}
	
	public static void main(String[] args) throws IOException {
		ByteBuf out = Unpooled.buffer(); // 堆缓冲区
		UserInfo userInfo1 = new UserInfo("lengyul", 100001);
		encode(userInfo1, out);
		System.out.println("[MessagePack] out length is " + out.readableBytes());
		System.out.println(out.getInt(0)); //从0开始读4个字节 对象序列化后的长度
		
		int length = out.readInt();
		ByteBuf dst = Unpooled.copiedBuffer(out.readBytes(length));
		byte[] bytes = dst.array();
		
		
		System.out.println(decode(bytes)); // 类似JSON
		System.out.println(decode(bytes, UserInfo.class));
		
		
		/*List<UserInfo> lists = new ArrayList<>();
		UserInfo userInfo1 = new UserInfo("lengyul", 100001);
		UserInfo userInfo2 = new UserInfo("lengyul", 100002);
		lists.add(userInfo1);lists.add(userInfo2);
		System.out.println(lists.getClass());
		//String str  = "{\"name\":\"lengyul\",\"id\":100001}";
		encode(lists, out);
		int length = out.readInt();
		ByteBuf dst = Unpooled.copiedBuffer(out.readBytes(length));
		byte[] bytes = dst.array();
		System.out.println(decodeTpl(bytes, Templates.TString));*/
	}
	
}
