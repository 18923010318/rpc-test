package proxy;

import client.NettyClient;
import common.protocol.RpcRequestProtocol;
import common.protocol.RpcResponseProtocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;


/**
 * 客户端代理
 *
 * @author zhangwenhao
 * @date 2021/11/25
 */
public class ClientServiceProxy<T> implements InvocationHandler {

    private final Class<T> clazz;
    private NettyClient nettyClient;

    public ClientServiceProxy(Class<T> clazz) throws Exception {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InterruptedException {

        RpcRequestProtocol request = new RpcRequestProtocol();
        String requestId = UUID.randomUUID().toString();

        String className = clazz.getName();
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();

        request.setRequestId(requestId);
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameterTypes(parameterTypes);
        request.setParameters(args);

        System.out.println("类：" + className + "执行方法：" + methodName + "参数：" + Arrays.toString(args) + "requestId" + requestId);

        //开启Netty 客户端，直连
        if (nettyClient == null) {

            nettyClient = new NettyClient("172.16.0.96", 8888);

            System.out.println("开始连接服务端：" + new Date());
            nettyClient.connect();
        }
        RpcResponseProtocol response = nettyClient.send(request);
        System.out.println("请求调用返回结果：" + response.getResult());
        return response.getResult();
    }
}
