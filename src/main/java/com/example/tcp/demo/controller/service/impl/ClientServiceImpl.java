package com.example.tcp.demo.controller.service.impl;

import com.example.tcp.demo.channel.CtrlAddressChannel;
import com.example.tcp.demo.controller.service.IClientService;
import com.example.tcp.demo.rabbit.IRabbitService;
import com.example.tcp.demo.rabbit.constant.RabbitExchangeConstant;
import com.example.tcp.demo.rabbit.constant.RabbitQueueConstant;
import com.example.tcp.demo.rabbit.constant.RabbitUrlConstant;
import com.example.tcp.demo.rabbit.domain.RabbitMessage;
import com.example.tcp.demo.redis.MRedisUtil;
import com.example.tcp.demo.redis.RedisConstant;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("clientService")
public class ClientServiceImpl implements IClientService {

    @Resource
    MRedisUtil mRedisUtil;
    @Resource(name = "tcpRabbitService")
    IRabbitService rabbitService;

    @Override
    public String sendHeartBeatToClient(String ctrlAddress) {
        String rs = "";
        Channel channel = CtrlAddressChannel.getInstance().get(ctrlAddress);
        if (!ObjectUtils.isEmpty(channel)) {
            channel.writeAndFlush(new TextWebSocketFrame("服务端发送消息成功"));
            rs = "发送成功";
            sendSuccessCallback(ctrlAddress);
        } else {
            sendFailCallback(ctrlAddress);
            rs = "发送失败";
        }
        return rs;
    }

    private void sendSuccessCallback(String ctrlAddress) {
//        mRedisUtil.put(RedisConstant.TCP_TEST + ctrlAddress, 0, 5, TimeUnit.MINUTES);
        mRedisUtil.remove(RedisConstant.TCP_TEST + ctrlAddress);
    }

    private void sendFailCallback(String ctrlAddress) {
        Integer value = mRedisUtil.get(RedisConstant.TCP_TEST + ctrlAddress);
        if (ObjectUtils.isEmpty(value)) {
            value = 1;
        } else {
            value = value + 1;
        }
        Integer maxTimes = 20;
        //到达极限次数保存记录
        if (value.equals(maxTimes)) {
            mRedisUtil.remove(RedisConstant.TCP_TEST + ctrlAddress);
            log.info("客户端：" + ctrlAddress + "已经尝试了" + maxTimes + "次，最终失败");
            return;
        }
        log.info("客户端：" + ctrlAddress + "已经尝试了" + value + "次");
        mRedisUtil.put(RedisConstant.TCP_TEST + ctrlAddress, value, 5, TimeUnit.MINUTES);
        RabbitMessage rabbitMessage = new RabbitMessage().setUrl(RabbitUrlConstant.LEVEL1_URL)
                .setContent(ctrlAddress);
        rabbitService.send(RabbitExchangeConstant.EXCHANGE_TCP, RabbitQueueConstant.QUEUE_TCP, rabbitMessage, 10);
    }
}
