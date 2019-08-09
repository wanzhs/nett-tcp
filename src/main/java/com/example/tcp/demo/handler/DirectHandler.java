package com.example.tcp.demo.handler;

import com.example.tcp.demo.channel.ChannelAttr;
import com.example.tcp.demo.channel.MAttributeKey;
import com.example.tcp.demo.channel.MDirectUtil;
import com.example.tcp.demo.frame.MDirect;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
@ChannelHandler.Sharable
public class DirectHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Resource
    DirectMessageDispatcher messageDispatcher;

    /**
     * @author:wanzhongsu
     * @description: 数据分发
     * @date:2019/8/9 15:37
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
        if (in == null || ctx == null) {
            return;
        }
        MDirect mDirect = new MDirect();
        /**判断包头*/
        int head = in.readByte();
        if (head != 0x63) {
            return;
        }
        /**跳过前面3个字节占时无用*/
        in.skipBytes(3);
        /**获取长度*/
        int len = in.readInt();
        /**跳过4个字节*/
        in.skipBytes(4);
        /** 获取集控器地址*/
        byte[] ctrlAddress = new byte[16];
        in.readBytes(ctrlAddress);
        /**跳过预留字段*/
        in.skipBytes(4);
        int reableBytes = in.readableBytes();
        if (reableBytes != len) {
            return;
        }
        /**读取数据域*/
        byte[] payload = new byte[len];
        in.readBytes(payload);
        ChannelAttr.getInstatnce().setAttr(ctx.channel(), MAttributeKey._CTRLADDR, ctrlAddress); //将通道的属性设置为集控器地址
        mDirect.setCtrlAddress(ctrlAddress).setPayload(payload);
        messageDispatcher.dispatch(ctx, mDirect);
    }

    /**
     * @author:wanzhongsu
     * @description: tcp网络中断
     * @date:2019/8/9 17:01
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        byte[] ctrlAddress = ChannelAttr.getInstatnce().getAttr(ctx.channel(), MAttributeKey._CTRLADDR, null);
        String ctrlAddressStr = MDirectUtil.getInstatnce().ctrlAddressToString(ctrlAddress);
        log.info(String.format("集控器,%s,网络端口连接", ctrlAddressStr));
        ctx.channel().closeFuture();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                byte[] ctrlAddress = ChannelAttr.getInstatnce().getAttr(ctx.channel(), MAttributeKey._CTRLADDR, null);
                if (ctrlAddress != null) {
                    String ctrlAddressStr = MDirectUtil.getInstatnce().ctrlAddressToString(ctrlAddress);
                    log.info(String.format("集控器,%s,关闭通道", ctrlAddressStr));
                }
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * @author:luqi
     * @description: 发生了错误了
     * @date:2018/11/6_10:33
     * @param:
     * @return:
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        byte[] ctrlAddress = ChannelAttr.getInstatnce().getAttr(ctx.channel(), MAttributeKey._CTRLADDR, null);
        if (ctrlAddress != null) {
            String ctrlAddressStr = MDirectUtil.getInstatnce().ctrlAddressToString(ctrlAddress);
            log.info("出错了！！！！,集控器%s,%s"
                    + ctrlAddressStr, cause);
        } else {

        }
    }

}
