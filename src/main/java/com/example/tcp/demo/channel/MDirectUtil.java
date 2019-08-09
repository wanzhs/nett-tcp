package com.example.tcp.demo.channel;


import com.example.tcp.demo.frame.MDirect;
import com.google.protobuf.GeneratedMessageV3;

import java.io.UnsupportedEncodingException;

/**
 * @author:wanzhongsu
 * @description: 工具类
 * @date:2019/8/9 16:41
 */
public class MDirectUtil {

    private MDirectUtil() {
    }

    public static MDirectUtil getInstatnce() {
        return MDirectUtil.Singleton.INSTANCE.getInstatnce();
    }

    private enum Singleton {
        INSTANCE;
        private MDirectUtil singleton;

        //JVM保证只调用一次
        Singleton() {
            singleton = new MDirectUtil();
        }

        public MDirectUtil getInstatnce() {
            return singleton;
        }
    }

    /**
     * @author:wanzhongsu
     * @description: 构造 direct
     * @date:2019/8/9 16:54
     */
    public MDirect builder(GeneratedMessageV3 payload, byte[] ctrlAddress) {
        byte[] payloadBytes = payload.toByteArray();
        MDirect mDirect = new MDirect().setPayload(payloadBytes).setCtrlAddress(ctrlAddress);
        return mDirect;
    }

    /**
     * @author:wanzhongsu
     * @description: 集控器地址转String
     * @date:2019/8/9 16:41
     */
    public String ctrlAddressToString(byte[] ctrlAddress) {
        try {
            String rs = new String(ctrlAddress, "UTF-8");
            return rs;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @author:wanzhongsu
     * @description: 集控器地址string转byte
     * @date:2019/8/9 16:42
     */
    public byte[] ctrlAddressToByte(String ctrlAddress) {
        try {
            byte[] rs = ctrlAddress.getBytes("utf-8");
            return rs;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
