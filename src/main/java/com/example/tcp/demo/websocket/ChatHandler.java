package com.example.tcp.demo.websocket;

import com.example.tcp.demo.channel.ChannelAttr;
import com.example.tcp.demo.channel.CtrlAddressChannel;
import com.example.tcp.demo.channel.MAttributeKey;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 处理消息的 handler
 * TextWebSocketFrame： 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 *
 * @author John
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于记录和管理所有客户端的channel
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static JsonParser jsonParser = new JsonParser();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 获取客户端传过来的消息
        String content = msg.text();
        String ctrlAddress = jsonParser.parse(content).getAsJsonObject().get("id").getAsString();
        content = jsonParser.parse(content).getAsJsonObject().get("value").getAsString();
        for (Channel channel : clients) {
            channel.writeAndFlush(new TextWebSocketFrame("客戶端：" + ctrlAddress + "连接成功"));
        }
        ChannelAttr.getInstatnce().setAttr(ctx.channel(), MAttributeKey._CLIENTId, ctrlAddress); //将通道的属性设置为集控器地址
        CtrlAddressChannel.getInstance().add(ctrlAddress, ctx.channel());
        //删除同一通道的其他连接地址
        purgeChannel(ctrlAddress, ctx.channel());
    }

    private void purgeChannel(String ctrlAddress, Channel channel) {
        ConcurrentHashMap<String, Channel> map = CtrlAddressChannel.getInstance().getMap();
        Enumeration<String> keys = map.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Channel value = map.get(key);
            if (!ctrlAddress.equals(key) && value.equals(channel)) {
                map.remove(key);
            }
        }
    }

    /**
     * 当客户端连接服务端后，（打开连接)
     * 获取客户端的channel，并且放到channelGroup中去进行管理
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        clients.add(ctx.channel());
        String ctrlAddress = ChannelAttr.getInstatnce().getAttr(ctx.channel(), MAttributeKey._CLIENTId, null);
        System.out.println("handler......添加");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved,ChannelGroup会自动移除对应的客户端channel
        System.out.println("handler......移除");
        clients.remove(ctx.channel());
        System.out.println("客户端断开，channel对应的长id为："
                + ctx.channel().id().asLongText());
        System.out.println("客户端断开，channel对应的短id为："
                + ctx.channel().id().asShortText());
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
