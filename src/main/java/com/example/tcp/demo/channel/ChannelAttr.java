package com.example.tcp.demo.channel;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author:wanzhongsu
 * @description: 管道树形管理
 * @date:2019/8/9 16:04
 */
@Slf4j
public class ChannelAttr {

    private ChannelAttr() {
    }

    public static ChannelAttr getInstatnce() {
        return Singleton.INSTANCE.getInstatnce();
    }

    private enum Singleton {
        INSTANCE;
        private ChannelAttr singleton;

        //JVM保证只调用一次
        Singleton() {
            singleton = new ChannelAttr();
        }

        public ChannelAttr getInstatnce() {
            return singleton;
        }
    }

    public <T> void setAttr(Channel channel, AttributeKey<T> key, T value) {
        Attribute<T> attribute = channel.attr(key);
        attribute.setIfAbsent(value);
    }


    public <T> T getAttr(Channel channel, AttributeKey<T> key, T defaultValue) {
        Attribute<T> attribute = channel.attr(key);
        T attr = attribute.get();
        if (attr == null) {
            return defaultValue;
        } else {
            return attr;
        }
    }
}
