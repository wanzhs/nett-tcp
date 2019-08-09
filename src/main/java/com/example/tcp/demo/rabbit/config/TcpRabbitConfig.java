package com.example.tcp.demo.rabbit.config;

import com.example.tcp.demo.rabbit.constant.RabbitExchangeConstant;
import com.example.tcp.demo.rabbit.constant.RabbitQueueConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:wanzhongsu
 * @description: mq配置文件
 * @date:2019/8/9 10:11
 */
@Slf4j
@Configuration
public class TcpRabbitConfig {

    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.virtual-host-tcp}")
    private String virtualHostTcp;

    @Value("${rabbitmq.publisher-confirms}")
    private boolean publisherConfirms;

    @Value("${rabbitmq.publisher-returns}")
    private boolean publisherReturns;


    /********  tcp虚拟主机配置  **********/
    @Bean("tcpConnectionFactory")
    public ConnectionFactory tcpConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        connectionFactory.setVirtualHost(virtualHostTcp);
        return connectionFactory;
    }

    @Bean("tcpRabbitTemplate")
    public RabbitTemplate jxRabbitTemplate(@Qualifier("tcpConnectionFactory") ConnectionFactory tcpConnectionFactory) {
        RabbitTemplate template = new RabbitTemplate(tcpConnectionFactory);
        return template;
    }

    @Bean("tcpRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory
    jxRabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer,
                                     @Qualifier("tcpConnectionFactory") ConnectionFactory tcpConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setConcurrentConsumers(10);
        factory.setPrefetchCount(10);
        configurer.configure(factory, tcpConnectionFactory);
        return factory;
    }

    /*******tcp队列1配置*******/
    @Bean
    public String tcpExchange(@Qualifier("tcpConnectionFactory") ConnectionFactory tcpConnectionFactory) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        Channel channel = tcpConnectionFactory.createConnection().createChannel(false);
        try {
            channel.exchangeDeclare(RabbitExchangeConstant.EXCHANGE_TCP, "x-delayed-message", true, false, false, args);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (Exception e) {
                log.error("mq channel close fail", e);
            }
        }
        return RabbitExchangeConstant.EXCHANGE_TCP;
    }

    @Bean
    public String tcpQueue(@Qualifier("tcpConnectionFactory") ConnectionFactory tcpConnectionFactory) {
        Channel channel = tcpConnectionFactory.createConnection().createChannel(false);
        try {
            channel.queueDeclare(RabbitQueueConstant.QUEUE_TCP, true, false, false, null);
            channel.queueBind(RabbitQueueConstant.QUEUE_TCP, RabbitExchangeConstant.EXCHANGE_TCP, RabbitQueueConstant.QUEUE_TCP);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (Exception e) {
                log.error("mq channel close fail", e);
            }
        }
        return RabbitQueueConstant.QUEUE_TCP;
    }

    /*******tcp队列n配置*******/

    /********  其他虚拟主机配置  **********/
}
