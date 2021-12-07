package common.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:22
 */
@Data
@ToString
public class RpcResponseProtocol {
    /**
     * 响应ID
     */
    private String requestId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 返回的结果
     */
    private Object result;
}
