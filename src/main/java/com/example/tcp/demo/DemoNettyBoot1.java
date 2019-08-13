package com.example.tcp.demo;

import com.example.tcp.demo.websocket.WSServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DemoNettyBoot1 implements CommandLineRunner {
    @Value("${netty.port}")
    private int NETTY_PORT;
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup(50);//50个线程工作组
    private Channel channel;

    @Resource
    WSServerInitializer serverInitializer;

    private void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture future = null;
        try {
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress(NETTY_PORT));
            bootstrap.childHandler(serverInitializer).option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = bootstrap.bind().syncUninterruptibly();// 配置完成，开始绑定server，通过调用sync同步方法阻塞直到绑定成功
            channel = future.channel();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (future != null && future.isSuccess()) {
                log.info("netty 启动 ok");
            } else {
                log.info("netty 启动 error");
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        start();
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if (channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
