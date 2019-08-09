package com.example.tcp.demo.frame;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author:wanzhongsu
 * @description: 协议编码器
 * @date:2019/8/9 14:49
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class MDirectEncoder extends MessageToMessageEncoder<MDirect> {


    @Override
    protected void encode(ChannelHandlerContext ctx, MDirect msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().ioBuffer();
        byte[] payload = msg.getPayload();
        /**起始符**/
        byteBuf.writeByte(0x63);
        /**报文头长度*/
        byteBuf.writeByte(0x20);
        /**协议版本号*/
        byteBuf.writeByte(0x03);
        /**消息体编码类型*/
        byteBuf.writeByte(0x01);
        /**消息体长度*/
        byteBuf.writeInt(payload.length);
        /**消息类型*/
        byteBuf.writeByte(0xf);
        /**报文的扩展信息*/
        byteBuf.writeByte(0);
        /**预留*/
        byteBuf.writeShort(0x00);
        /**集控器地址*/
        byteBuf.writeBytes(msg.getCtrlAddress());
        /**预留*/
        byteBuf.writeInt(0x00);
        /**报文消息体*/
        byteBuf.writeBytes(payload);
        out.add(byteBuf);
    }
}
