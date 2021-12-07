package common.future;

import common.protocol.RpcResponseProtocol;

/**
 * @author: zhangwenhao
 * @date: 2021/11/24 17:00
 */
public class DefaultFuture {
    private RpcResponseProtocol rpcResponse;
    private volatile boolean isSucceed = false;
    private final Object object = new Object();

    public RpcResponseProtocol getRpcResponse(int timeout) {
        synchronized (object) {
            while (!isSucceed) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return rpcResponse;
        }
    }

    public void setResponse(RpcResponseProtocol response) {
        if (isSucceed) {
            return;
        }
        synchronized (object) {
            this.rpcResponse = response;
            this.isSucceed = true;
            object.notify();
        }
    }
}
