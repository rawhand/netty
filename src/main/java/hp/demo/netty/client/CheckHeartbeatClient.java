package hp.demo.netty.client;

import hp.demo.netty.channelinitalizer.CheckWriteIdleChannelInitializer;
import hp.demo.netty.config.CheckHeartbeatClientEnable;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Conditional(CheckHeartbeatClientEnable.class)
@Component
public class CheckHeartbeatClient implements ApplicationRunner{
    private final static Logger LOGGER = LoggerFactory.getLogger(CheckHeartbeatClient.class);

    public void start() throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new CheckWriteIdleChannelInitializer());

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 33333).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            Future future = workerGroup.shutdownGracefully();
            future.await();
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        while (true) {
            try {
                start();
            } catch (InterruptedException e) {
                LOGGER.warn("run 【{}】 exception 【{}】", this.getClass(), e.toString(), e);
            }
            Thread.sleep(5000);
        }
    }
}
