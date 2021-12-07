package common.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:23
 */
@Data
@ToString
public class RpcRequestProtocol {
    /**
     * 请求对象的ID
     */
    private String requestId;
    /**
     * 类名
     */
    private String className;
    /**
     * 方法名
     */
    private String methodName;
    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;
    /**
     * 入参
     */
    private Object[] parameters;
}
