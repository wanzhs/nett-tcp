package com.example.tcp.demo.frame;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:wanzhongsu
 * @description: 特来电充电协议
 * @date:2019/8/9 14:48
 */
@Data
@Accessors(chain = true)
public class MFrameDirect {
    /**
     * 固定头0x63
     */
    private byte startSymbol;
    /**
     * 报文头长度 0x20
     */
    private byte headerLength;
    /**
     * 协议版本号，第一次版为 0x03
     */
    private byte protocolVersion;
    /**
     * 消息编码体 0x01
     */
    private byte encodingType;
    /**
     * 数据域长度
     */
    private int payloadLength;
    /**
     * 消息类型
     */
    private byte messageType;
    /**
     * 报文的扩展类型
     */
    private byte extendMessageType;
    /**
     * 预留 0x00
     */
    private short reserved1;
    /**
     * 集控器地址 固定16位
     */
    private byte[] ctrlAddress;
    /**
     * 预留 固定4位
     */
    private byte[] reserved2;
    /**
     * 报文消息体 长度等于payloadLength
     */
    private byte[] payload;
}
