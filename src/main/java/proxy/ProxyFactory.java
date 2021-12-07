package proxy;

import java.lang.reflect.Proxy;

/**
 * @author: zhangwenhao
 * @date: 2021/11/25 14:58
 */
public class ProxyFactory {
    public static <T> T getClientProxy(Class<T> interfaceClass) throws Exception {
        return  (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ClientServiceProxy<>(interfaceClass));
    }

    public static <T> T getServerProxy(Class<T> interfaceClass) throws Exception {
        return  (T)Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new ServerServiceProxy<>(interfaceClass));
    }
}
