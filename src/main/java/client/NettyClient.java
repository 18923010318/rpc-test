package client;

import client.handler.ClientChildHandler;
import common.codec.MetaRpcDecoder;
import common.codec.MetaRpcEncoder;
import common.future.DefaultFuture;
import common.protocol.RpcRequestProtocol;
import common.protocol.RpcResponseProtocol;
import common.serializer.impl.FasterJsonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:51
 */
public class NettyClient {
    private EventLoopGroup eventLoopGroup;
    private Channel channel;
    private ClientChildHandler clientHandler;
    private String host;
    private Integer port;
    private static final int MAX_RETRY = 5;

    public NettyClient(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        clientHandler = new ClientChildHandler();
        eventLoopGroup = new NioEventLoopGroup();
        //启动类
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                //指定传输使用的Channel
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        //添加编码器
                        pipeline.addLast(new MetaRpcEncoder(RpcRequestProtocol.class, new FasterJsonSerializer()));
                        //添加解码器
                        pipeline.addLast(new MetaRpcDecoder(RpcResponseProtocol.class, new FasterJsonSerializer()));
                        //请求处理类
                        pipeline.addLast(clientHandler);
                    }
                });

        // 建立连接
        connect(bootstrap, host, port, MAX_RETRY);
    }

    /**
     * 失败重连机制
     *
     * @param bootstrap
     * @param host
     * @param port
     * @param retry
     */
    private void connect(Bootstrap bootstrap, String host, int port, int retry) throws InterruptedException {

        ChannelFuture channelFuture = bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接服务端成功");
            } else if (retry == 0) {
                System.out.println("重试次数已用完，放弃连接");
            } else {
                //第几次重连：
                int order = (MAX_RETRY - retry) + 1;
                //本次重连的间隔
                int delay = 1 << order;
                System.out.println("{} : 连接失败，第 {} 重连...." + new Date() + "  " + order);
                bootstrap.group().schedule(() -> {
                    try {
                        connect(bootstrap, host, port, retry - 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, delay, TimeUnit.SECONDS);
            }
        }).sync();
        channel = channelFuture.channel();
    }

    /**
     * 发送消息
     *
     * @param request
     * @return
     */
    public RpcResponseProtocol send(final RpcRequestProtocol request) {

        System.out.println("发送消息request: " + request);
        channel.writeAndFlush(request);
        clientHandler.setRequestId(request.getRequestId(),  new DefaultFuture());

        return clientHandler.getRpcResponse(request.getRequestId());
    }

    @PreDestroy
    public void close() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
    }
}
