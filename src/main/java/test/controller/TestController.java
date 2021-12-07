package test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.client.RpcClientTest;

/**
 * @author: zhangwenhao
 * @date: 2021/12/7 10:33
 */
@RestController
public class TestController {
    @Autowired
    RpcClientTest rpcClientTest;

    @GetMapping("/rpc")
    public String rpcRequest(@RequestParam String type) throws Exception {
        return rpcClientTest.rpcRequest(type);
    }
}
