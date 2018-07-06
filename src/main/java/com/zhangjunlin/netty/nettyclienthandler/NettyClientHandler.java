package com.zhangjunlin.netty.nettyclienthandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * @author Tony
 * @description
 * @createTime 2018/7/6 11:51
 */
public class NettyClientHandler extends ChannelHandlerAdapter {

    private ByteBuf firstMessage;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        byte[] data = "你好，服务器".getBytes();
        firstMessage = Unpooled.buffer();
        firstMessage.writeBytes(data);
        ctx.writeAndFlush(firstMessage);
        System.err.println("客户端发送消息:你好，服务器");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        String rev = getMessage(buf);
        System.err.println("客户端收到服务器消息:" + rev);
    }

    private String getMessage(ByteBuf buf) {
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            return new String(con, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
