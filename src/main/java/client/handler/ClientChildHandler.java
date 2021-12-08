package client.handler;

import common.future.DefaultFuture;
import common.protocol.RpcRequestProtocol;
import common.protocol.RpcResponseProtocol;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义的输入流处理器
 *
 * @author: zhangwenhao
 * @date: 2021/11/24 16:56
 */
public class ClientChildHandler extends ChannelDuplexHandler {
    /**
     * 使用Map维护请求对象ID与响应结果Future的映射关系
     */
    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcResponseProtocol) {
            // 获取响应对象
            RpcResponseProtocol response = (RpcResponseProtocol) msg;
            DefaultFuture defaultFuture = futureMap.get(response.getRequestId());
            // 将结果写入DefaultFuture
            defaultFuture.setResponse(response);
        }
        super.channelRead(ctx,msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof RpcRequestProtocol) {
            RpcRequestProtocol request = (RpcRequestProtocol) msg;
            // 发送请求对象之前，先把请求ID保存下来，并构建一个与响应Future的映射关系
            futureMap.putIfAbsent(request.getRequestId(), new DefaultFuture());
        }
        super.write(ctx, msg, promise);
    }

    /**
     * 获取响应结果
     *
     * @param requestId
     * @return
     */
    public RpcResponseProtocol getRpcResponse(String requestId) {
        try {
            DefaultFuture future = futureMap.get(requestId);
            return future.getRpcResponse(5000);
        } finally {
            //获取成功以后，从map中移除
            futureMap.remove(requestId);
        }
    }

    public void setRequestId(String requestId, DefaultFuture future){
        futureMap.put(requestId, future);
    }

}
