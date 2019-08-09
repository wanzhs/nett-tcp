package com.example.tcp.demo.base;

public interface ITcpReceiverService {
    /**
     * @author:wanzhongsu
     * @description: 处理mq消息
     * @date:2019/8/9 10:39
     */
    void handleMqMessage(Object content);
}
