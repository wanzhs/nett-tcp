package com.example.tcp.demo.base.impl;

import com.example.tcp.demo.base.ITcpReceiverService;
import org.springframework.stereotype.Service;

@Service("tcpReceiverService")
public class TcpReceiverServiceImpl implements ITcpReceiverService {
    @Override
    public void handleMqMessage(Object content) {

    }
}
