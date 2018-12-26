package com.netty.spdy;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;

public class DefaultServerProvider  implements ServerProvider{
	
	//定义所有的 ServerProvider 实现的协议
	private static final List<String> PROTOCOLS =
            Collections.unmodifiableList(Arrays.asList("spdy/2", "spdy/3", "http/1.1"));  //1
	
	private String protocol;
	
	//设置如果 SPDY 协议失败了就转到 http/1.1
	@Override
	public void unsupported() {
		protocol = "http/1.1";
	}

	//设置如果 SPDY 协议失败了就转到 http/1.1
	@Override
	public List<String> protocols() {
		return PROTOCOLS;
	}

	//设置选择的协议
	@Override
	public void protocolSelected(String protocol) {
		protocol = "http/1.1";
	}
	
	//返回选择的协议
	public String getSelectedProtocol() {
        return protocol; 
    }

}
