package com.example.tcp.demo.base.impl;

import com.example.tcp.demo.base.ITcpReceiverService;
import com.example.tcp.demo.controller.service.IClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("tcpReceiverService")
public class TcpReceiverServiceImpl implements ITcpReceiverService {

    @Resource
    IClientService clientService;

    @Override
    public void handleMqMessage(Object content) {
        if (content instanceof String) {
            String ctrlAddress = (String) content;
            clientService.sendHeartBeatToClient(ctrlAddress);
        }
    }
}
