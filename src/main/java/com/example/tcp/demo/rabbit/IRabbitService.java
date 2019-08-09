package com.example.tcp.demo.rabbit;


import com.example.tcp.demo.rabbit.domain.RabbitMessage;

public interface IRabbitService {

    /**
     * 默认的发送方式
     */
    void send(String exchange, String queueName, RabbitMessage message);


    /**
     * 延时发送 秒
     */
    void send(String exchange, String queueName, RabbitMessage message, int time);

    /**
     * 发送json
     */
    void sendJson(String exchange, String queueName, Object object);
}
