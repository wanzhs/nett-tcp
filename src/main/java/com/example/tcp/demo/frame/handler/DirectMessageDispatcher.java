package com.example.tcp.demo.frame.handler;


import com.example.tcp.demo.channel.MDirectUtil;
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
        String ctrlAddress = MDirectUtil.getInstatnce().ctrlAddressToString(msg.getCtrlAddress());
        String content = MDirectUtil.getInstatnce().ctrlAddressToString(msg.getPayload());
        log.info(String.format("集控器：%s,信息：%s", ctrlAddress, content));
        //向客户端发送信息
//        ctx.channel().writeAndFlush(null);
    }

}
