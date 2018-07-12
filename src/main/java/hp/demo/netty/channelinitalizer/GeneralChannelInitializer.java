package hp.demo.netty.channelinitalizer;

import hp.demo.netty.handler.DiscardServerHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class GeneralChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static final StringDecoder decoder = new StringDecoder(CharsetUtil.UTF_8);
    private static final StringEncoder encoder = new StringEncoder(CharsetUtil.UTF_8);
    private static final DiscardServerHandler server = new DiscardServerHandler();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("delimiter", new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\r\n".getBytes())));
        channelPipeline.addLast("decoder", decoder);
        channelPipeline.addLast("encoder", encoder);
        channelPipeline.addLast("server", server);
    }
}
