package annotation;

import common.protocol.RpcRequestProtocol;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

/**
 * @author: zhangwenhao
 * @date: 2021/12/7 15:55
 */
@Component
@Aspect
public class ServerProductAspect {

    @Before("@annotation(Product)")
    public void doBefore(JoinPoint joinPoint){
        RpcRequestProtocol request = new RpcRequestProtocol();
        String requestId = UUID.randomUUID().toString();
    }

}
