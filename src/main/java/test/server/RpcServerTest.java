package test.server;

import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author: zhangwenhao
 * @date: 2021/12/6 17:24
 */
@Service
public class RpcServerTest implements RpcServer {
    @Override
    public String proServer(String type) {
        System.out.println("收到RPC调用");
        if (Objects.equals(type, "new")) {
            return "a new project";
        }

        return " old project";

    }
}
