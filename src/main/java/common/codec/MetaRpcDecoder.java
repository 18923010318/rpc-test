package common.codec;

import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:38
 */
public class MetaRpcDecoder extends ByteToMessageDecoder {

    private final Class<?> clazz;
    private final Serializer serializer;

    public MetaRpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("decode 1 " + byteBuf.readerIndex() +"  "+  byteBuf.writerIndex());
        // 因为之前编码的时候写入一个Int型，4个字节来表示长度
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        // 标记当前读的位置
        byteBuf.markReaderIndex();
        int dataLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[dataLength];
        // 将byteBuf中的数据读入data字节数组
        byteBuf.readBytes(data);
        Object obj = serializer.deserialize(clazz, data);
        list.add(obj);

        System.out.println("decode 2 " + byteBuf.readerIndex() +"  "+  byteBuf.writerIndex());
    }

}
