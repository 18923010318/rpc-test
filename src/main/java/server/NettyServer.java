package server;

import common.codec.MetaRpcDecoder;
import common.codec.MetaRpcEncoder;
import common.protocol.RpcRequestProtocol;
import common.protocol.RpcResponseProtocol;
import common.serializer.impl.FasterJsonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import server.handler.ServerChildHandler;

import javax.annotation.PreDestroy;

/**
 * @author: zhangwenhao
 * @date: 2021/11/24 17:49
 */
public class NettyServer {
    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private ServerChildHandler serverHandler;

    public void start() throws Exception {

        serverHandler = new ServerChildHandler();
        //负责处理客户端连接的线程池
        boss = new NioEventLoopGroup();
        //负责处理读写操作的线程池
        worker = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .option(ChannelOption.SO_BACKLOG, 300)
                //有数据立即发送
                .option(ChannelOption.TCP_NODELAY, true)
                //保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //添加解码器
                        pipeline.addLast(new MetaRpcEncoder(RpcResponseProtocol.class, new FasterJsonSerializer()));
                        //添加编码器
                        pipeline.addLast(new MetaRpcDecoder(RpcRequestProtocol.class, new FasterJsonSerializer()));
                        //添加请求处理器
                        pipeline.addLast(serverHandler);

                    }
                });
        bind(serverBootstrap, 8888);
    }

    /**
     * 如果端口绑定失败，端口数+1,重新绑定
     *
     * @param serverBootstrap
     * @param port
     */
    public void bind(final ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口绑定成功 port:" + port);
            } else {
                System.out.println("端口绑定失败 port:" + port);
                bind(serverBootstrap, port + 1);
            }
        });
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        boss.shutdownGracefully().sync();
        worker.shutdownGracefully().sync();
        System.out.println("关闭Netty");
    }
}
