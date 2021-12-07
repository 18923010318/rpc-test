package test.client;

import org.springframework.stereotype.Service;
import proxy.ProxyFactory;
import test.server.RpcServer;

/**
 * @author: zhangwenhao
 * @date: 2021/12/6 17:20
 */
@Service
public class RpcClientTest {
    public void rpcRequest() throws Exception {
        RpcServer rpcServer = ProxyFactory.getClientProxy(RpcServer.class);
        System.out.println(rpcServer.proServer("\"new\""));
    }

    public String rpcRequest(String type) throws Exception {
        RpcServer rpcServer = ProxyFactory.getClientProxy(RpcServer.class);
        return rpcServer.proServer("\"new\"");
    }

}
