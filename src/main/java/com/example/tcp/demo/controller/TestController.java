package com.example.tcp.demo.controller;

import com.example.tcp.demo.controller.service.IClientService;
import com.example.tcp.demo.rabbit.constant.RabbitExchangeConstant;
import com.example.tcp.demo.rabbit.constant.RabbitQueueConstant;
import com.example.tcp.demo.rabbit.constant.RabbitUrlConstant;
import com.example.tcp.demo.rabbit.domain.RabbitMessage;
import com.example.tcp.demo.redis.RedisConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    IClientService clientService;

    @GetMapping("/send/{ctrlAddress}")
    public String sendByCode(@PathVariable("ctrlAddress") String ctrlAddress) {
        String rs = clientService.sendHeartBeatToClient(ctrlAddress);
        return rs;
    }
}
