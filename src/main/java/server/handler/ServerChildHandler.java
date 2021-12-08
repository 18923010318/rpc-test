package server.handler;

import common.protocol.RpcRequestProtocol;
import common.protocol.RpcResponseProtocol;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author: zhangwenhao
 * @date: 2021/11/24 17:51
 */
@ChannelHandler.Sharable
public class ServerChildHandler extends SimpleChannelInboundHandler<RpcRequestProtocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequestProtocol msg) {
        System.out.println("server处理器处理");
        RpcResponseProtocol rpcResponse = new RpcResponseProtocol();
        rpcResponse.setRequestId(msg.getRequestId());
        try {
            Object result = handler(msg);
            System.out.println("获取返回结果: " + result);
            rpcResponse.setResult(result);
        } catch (Throwable throwable) {
            rpcResponse.setError(throwable.toString());
            throwable.printStackTrace();
        }
        channelHandlerContext.writeAndFlush(rpcResponse);
    }

    /**
     * 服务端处理请求
     *
     * @param request
     * @return
     */
    private Object handler(RpcRequestProtocol request) throws Exception {
        Class<?> clazz = Class.forName(request.getClassName() + "Impl");
        Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
        System.out.println("类：" + clazz.getName() + "执行方法：" + method.getName());
        Object object = clazz.newInstance();
        return method.invoke(object, request.getParameters());
    }
}
