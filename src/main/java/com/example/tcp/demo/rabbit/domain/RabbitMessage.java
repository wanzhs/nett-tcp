package com.example.tcp.demo.rabbit.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author:wanzhongsu
 * @description: rabbitmq消息
 * @date:2019/8/9 10:09
 */
@Data
@Accessors(chain = true)
public class RabbitMessage implements Serializable {

    /**
     * 消息标识
     */
    private String url;

    /**
     * 消息内容
     */
    private Object content;

}
