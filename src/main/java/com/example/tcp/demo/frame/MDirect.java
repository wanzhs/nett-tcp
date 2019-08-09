package com.example.tcp.demo.frame;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author:wanzhongsu
 * @description: 协议对应的实体类
 * @date:2019/8/9 15:19
 */
@Data
@Accessors(chain = true)
public class MDirect {
    /**
     * 集控器地址 固定16位
     */
    private byte[] ctrlAddress;
    /**
     * 数据域
     */
    private byte[] payload;

}
