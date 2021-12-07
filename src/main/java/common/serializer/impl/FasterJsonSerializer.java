package common.serializer.impl;

import com.alibaba.fastjson.JSON;
import common.serializer.Serializer;

import java.io.IOException;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:25
 */
public class FasterJsonSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) throws IOException {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return JSON.parseObject(bytes, clazz);
    }
}
