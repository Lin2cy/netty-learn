package com.zhangjunlin.netty.server;

import com.zhangjunlin.netty.nettyserverhandler.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @author Tony
 * @description
 * @createTime 2018/7/6 09:52
 */
public class NettyServer {

    private int port;

    public NettyServer(int port) {
        this.port = port;
        bind();
    }

    private void bind() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(boss, worker);

            bootstrap.channel(NioServerSocketChannel.class);

            bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //最大连接数
            bootstrap.option(ChannelOption.TCP_NODELAY, true);//不延迟

            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); // 长连接


            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    ChannelPipeline p = socketChannel.pipeline();
                    p.addLast(new NettyServerHandler());// 添加NettyServerHandler，用来处理Server端接收和处理消息的逻辑
                }
            });


            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            if (channelFuture.isSuccess()) {
                System.err.println("启动Netty服务成功，端口号：" + this.port);
            }
            // 关闭连接
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            System.err.println("启动Netty服务异常，异常信息：" + e.getMessage());
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args){
        new NettyServer(10086);
    }



}
