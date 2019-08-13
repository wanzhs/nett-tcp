package com.example.tcp.demo.rabbit.receiver;

import com.example.tcp.demo.base.ITcpReceiverService;
import com.example.tcp.demo.rabbit.constant.RabbitQueueConstant;
import com.example.tcp.demo.rabbit.constant.RabbitUrlConstant;
import com.example.tcp.demo.rabbit.domain.RabbitMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author:wanzhongsu
 * @description: tcp队列监听处理
 * @date:2019/8/9 10:36
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitQueueConstant.QUEUE_TCP, containerFactory = "tcpRabbitListenerContainerFactory")
public class TcpToSelfReceiver {
    @Resource
    ITcpReceiverService tcpReceiverService;

    /**
     * @author:wanzhongsu
     * @description: rabbitmq消息回调
     * @date:2019/8/9 10:33
     */
    @RabbitHandler
    public void receiver(RabbitMessage rabbitMessage) {
        String url = rabbitMessage.getUrl();
        try {
            Object content = rabbitMessage.getContent();
            if (url.equals(RabbitUrlConstant.LEVEL1_URL)) {
                level1_handle(content);
            } else if (url.equals(RabbitUrlConstant.LEVEL2_URL)) {
                level2_handle(content);
            }
        } catch (Exception e) {
            log.error("处理mq接收的消息出现异常");
        }

    }


    /**
     * @author:wanzhongsu
     * @description: 类型1处理
     * @date:2019/8/9 10:34
     */
    private void level1_handle(Object content) {
        log.info("类型1待处理");
        tcpReceiverService.handleMqMessage(content);
    }

    /**
     * @author:wanzhongsu
     * @description: 类型2处理
     * @date:2019/8/9 10:34
     */
    private void level2_handle(Object content) {
        log.info("类型2待处理");
    }

}
