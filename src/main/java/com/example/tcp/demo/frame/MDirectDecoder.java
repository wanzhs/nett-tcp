package com.example.tcp.demo.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wanzhongsu
 * @description: 解码器
 * @date:2019/8/9 15:19
 */
@Slf4j
public class MDirectDecoder extends LengthFieldBasedFrameDecoder {

    public MDirectDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected MDirect decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in = (ByteBuf) super.decode(ctx, in);
        MDirect mDirect = new MDirect();
        if (in == null || ctx == null) {
            return null;
        }
        /**跳过前面4个字节占时无用*/
        in.skipBytes(4);
        /**获取长度*/
        int len = in.readInt();
        /**跳过4个字节*/
        in.skipBytes(4);
        /** 获取集控器地址*/
        byte[] ctrlAddress = new byte[16];
        in.readBytes(ctrlAddress);
        /**跳过预留字段*/
        in.skipBytes(4);
        /**读取数据域*/
        byte[] payload = new byte[len];
        in.readBytes(payload);
        mDirect.setCtrlAddress(ctrlAddress).setPayload(payload);
        ReferenceCountUtil.release(in);
        return mDirect;
    }
}
