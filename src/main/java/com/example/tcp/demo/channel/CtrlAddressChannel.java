package com.example.tcp.demo.channel;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CtrlAddressChannel implements BaseChannel<String, Channel> {

    private final ConcurrentHashMap<String, Channel> ctrlAddressMap = new ConcurrentHashMap();

    private CtrlAddressChannel() {
    }

    public static CtrlAddressChannel getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;
        private CtrlAddressChannel singleton;

        //JVM保证只调用一次
        Singleton() {
            singleton = new CtrlAddressChannel();
        }

        public CtrlAddressChannel getInstance() {
            return singleton;
        }
    }

    @Override
    public void add(String key, Channel value) {
        if (this.contains(key)) {
            Channel oldChannel = ctrlAddressMap.get(key);
            oldChannel.close();
            //
            log.info("集控器老的管道删除，{}", key);
            ctrlAddressMap.remove(key);
        }
        ctrlAddressMap.put(key, value);
        log.info("成功添加了,集控器{},id,{},进入管道", key, value.id());
    }

    @Override
    public Channel get(String key) {
        return ctrlAddressMap.get(key);
    }

    @Override
    public void remove(String key) {
        if (contains(key)) {
            log.info("删除了集控器通道,{}", key);
            ctrlAddressMap.remove(key);
        }

    }

    @Override
    public boolean contains(String key) {
        return ctrlAddressMap.containsKey(key);
    }

    @Override
    public int size() {
        return ctrlAddressMap.size();
    }

    public ConcurrentHashMap<String, Channel> getMap() {
        return ctrlAddressMap;
    }


}