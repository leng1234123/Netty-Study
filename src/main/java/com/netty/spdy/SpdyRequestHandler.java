package com.netty.spdy;

import com.netty.utils.HttpRequestHandler;

/**
 * 继承 HttpRequestHandler 共享相同的逻辑
 * 重写 HttpRequestHandler 的 getContent() 的实现
 * @author lengyul
 *
 */
public class SpdyRequestHandler extends HttpRequestHandler {
	   
	 	@Override
	    protected String getContent() {
	        return "This content is transmitted via SPDY\r\n";  //2
	    }
}
