package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author: zhangwenhao
 * @date: 2021/11/25 14:54
 */
public class ServerServiceProxy<T> implements InvocationHandler {

    private final Class<T> clazz;
    public ServerServiceProxy(Class<T> clazz) throws Exception {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("类：" + clazz.getName() + "执行方法："+ method.getName() + "参数：" + Arrays.toString(args));
        return method.invoke(proxy, args);
    }
}
