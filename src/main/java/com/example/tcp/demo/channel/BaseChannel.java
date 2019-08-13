package com.example.tcp.demo.channel;


/**
 * @author:wanzhongsu
 * @description: 频道基础类
 * @date:2019/8/12 8:59
 */
public interface BaseChannel<K, T> {

    void add(K key, T value);

    T get(K key);

    void remove(K key);

    boolean contains(K key);

    int size();
}
