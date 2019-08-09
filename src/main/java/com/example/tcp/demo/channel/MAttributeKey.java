package com.example.tcp.demo.channel;

import io.netty.util.AttributeKey;

/**
 * @author:wanzhongsu
 * @description: channel的属性
 * @date:2019/8/9 16:02
 */
public class MAttributeKey {
    /**
     * 集控器地址 16位地址
     */
    public final static AttributeKey<byte[]> _CTRLADDR = AttributeKey.valueOf("CTRLADDR");

}
