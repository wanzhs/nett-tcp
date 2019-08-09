package com.example.tcp.demo.handler;


import com.example.tcp.demo.frame.MDirect;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author:wanzhongsu
 * @description: 直流分发
 * @date:2019/8/9 17:01
 */
@Slf4j
@Component
public class DirectMessageDispatcher {

    public void dispatch(ChannelHandlerContext ctx, MDirect msg) {
        /**使用netty的工作线程，不需要自己弄线程池**/

    }

}
