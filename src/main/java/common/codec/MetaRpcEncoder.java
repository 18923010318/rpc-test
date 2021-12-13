package common.codec;

import common.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author: zhangwenhao
 * @date: 2021/11/22 18:34
 */
public class MetaRpcEncoder extends MessageToByteEncoder {

    private final Class<?> clazz;
    private final Serializer serializer;

    public MetaRpcEncoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        System.out.println("encode 1 " + byteBuf.readerIndex() +"  "+ byteBuf.writerIndex());
        if (clazz != null && clazz.isInstance(o)) {
            byte[] bytes = serializer.serialize(o);
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
            System.out.println("encode 2 " + byteBuf.readerIndex() +"  "+ byteBuf.writerIndex());
        }
    }
}
