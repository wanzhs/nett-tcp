package com.example.tcp.demo.controller;

import com.example.tcp.demo.controller.service.IClientService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    IClientService clientService;

    @CrossOrigin
    @GetMapping("/send/{ctrlAddress}")
    public String sendByCode(@PathVariable("ctrlAddress") String ctrlAddress) {
        String rs = clientService.sendHeartBeatToClient(ctrlAddress);
        return rs;
    }
}
