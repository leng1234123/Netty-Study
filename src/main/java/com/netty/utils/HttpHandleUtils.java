package com.netty.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;

public class HttpHandleUtils {
		
	/**
     * 获取传递的参数
     * @param ctx
     * @param fullHttpRequest
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, Object> getParamsFromChannel(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){
        HttpHeaders headers = fullHttpRequest.headers();
        String strContentType = headers.get("Content-Type").trim();
        System.out.println("文本类型:" + strContentType);
        Map<String, Object> mapReturnData = new HashMap<String, Object>();
        try {
        if (fullHttpRequest.method() == HttpMethod.GET) {
            // 处理get请求
            QueryStringDecoder decoder = new QueryStringDecoder(
                    fullHttpRequest.uri());
            Map<String, List<String>> parame = decoder.parameters();
            for (Entry<String, List<String>> entry : parame.entrySet()) {
                mapReturnData.put(entry.getKey(), entry.getValue().get(0));
            }
            System.out.println("GET方式:" + parame.toString());
        } else if (fullHttpRequest.method() == HttpMethod.POST) {
            // 处理POST请求
            if (strContentType.contains("form-data") || strContentType.contains("x-www-form-urlencoded")) {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(
                        new DefaultHttpDataFactory(false), fullHttpRequest);
                List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
                for (InterfaceHttpData data : postData) {
                    if (data.getHttpDataType() == HttpDataType.Attribute) {
                        MemoryAttribute attribute = (MemoryAttribute) data;
                        mapReturnData.put(attribute.getName(),
                                attribute.getValue());
                    }
                }
            }else if(strContentType.contains("text/plain") || strContentType.contains("application/json")){
            	 // 解析text数据
                ByteBuf content = fullHttpRequest.content();
                byte[] reqContent = new byte[content.readableBytes()];
                content.readBytes(reqContent);
                String strContent = new String(reqContent, "UTF-8");
                System.out.println("接收到的消息:" + strContent);
                JSONObject jsonParamRoot = JSONObject.parseObject(strContent);
                for (String key : jsonParamRoot.keySet()) {
                    mapReturnData.put(key, jsonParamRoot.get(key));
                }
            } else {
                FullHttpResponse response = new DefaultFullHttpResponse(
                		HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
                ctx.writeAndFlush(response).addListener(
                        ChannelFutureListener.CLOSE);
            }
            System.out.println("POST方式：" + mapReturnData.toString());
        }
        } catch (Exception e) {
			e.printStackTrace();
		}
        return mapReturnData;
    }
    
    
}
