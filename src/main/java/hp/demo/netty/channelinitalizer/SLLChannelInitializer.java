package hp.demo.netty.channelinitalizer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class SLLChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder decoder = new StringDecoder(CharsetUtil.UTF_8);
    private static final StringEncoder encoder = new StringEncoder(CharsetUtil.UTF_8);

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("decoder", decoder);
        channelPipeline.addLast("encoder", encoder);
    }
}
