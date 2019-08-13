package com.example.tcp.demo.websocket.backup;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;


/**
 * 	处理消息的 handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 * @author John
 *
 */
public class ChatHandler1 extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于记录和管理所有客户端的channel
    private static ChannelGroup clients=
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传过来的消息
        String content = msg.text();
        System.out.println("接收到的数据： "+content);
        for (Channel channel:clients) {
            channel.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息时间: ]"+
                    LocalDateTime.now()+", 消息为："+content));
        }
//		//以下方法与上面for循环一致
//		clients.writeAndFlush(new TextWebSocketFrame("[服务器接收到消息时间: ]"+
//		LocalDateTime.now()+", 消息为："+content));
//
    }

    /**
     * 	当客户端连接服务端后，（打开连接)
     * 	获取客户端的channel，并且放到channelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        System.out.println("handler......添加");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved,ChannelGroup会自动移除对应的客户端channel
        System.out.println("handler......移除");
        clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id为："
                +ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id为："
                +ctx.channel().id().asShortText());
    }



    //以下为验证过程添加，可以删除

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel......注册");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel......移除");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel......活跃");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel......不活跃");
        super.channelInactive(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel读取数据完毕......");
        super.channelReadComplete(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("用户事件触发......注册");
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("channel可写更改......");
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("异常捕获......");
        ctx.channel().closeFuture();
        super.exceptionCaught(ctx, cause);
    }
}
