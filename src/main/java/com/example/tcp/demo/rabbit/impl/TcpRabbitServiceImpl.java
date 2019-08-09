package com.example.tcp.demo.rabbit.impl;


import com.example.tcp.demo.rabbit.IRabbitService;
import com.example.tcp.demo.rabbit.domain.RabbitMessage;
import com.example.tcp.demo.utils.MJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:wanzhongsu
 * @description: rabbitmq发送
 * @date:2019/8/9 10:08
 */
@Slf4j
@Service("tcpRabbitService")
public class TcpRabbitServiceImpl implements IRabbitService {

    @Resource(name = "tcpRabbitTemplate")
    RabbitTemplate rabbitTemplate;

    @Resource
    MJsonUtil mJsonUtil;


    @Override
    public void send(String exchange, String queueName, RabbitMessage message) {
        rabbitTemplate.convertAndSend(exchange, queueName, message);
    }

    @Override
    public void send(String exchange, String queueName, RabbitMessage message, int time) {
        if (time <= 0) {
            //直接发送
            send(exchange, queueName, message);
        } else {
            int mTime = time * 1000;
            //延时发送
            MessagePostProcessor processor = new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setDelay(mTime);
                    return message;
                }
            };
            rabbitTemplate.convertAndSend(exchange, queueName, message, processor);
        }
    }

    @Override
    public void sendJson(String exchange, String queueName, Object object) {
        String jsonStr = mJsonUtil.objectToJsonStr(object);
        rabbitTemplate.convertAndSend(exchange, queueName, jsonStr);
    }


}
