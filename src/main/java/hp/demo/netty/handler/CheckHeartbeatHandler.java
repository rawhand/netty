package hp.demo.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class CheckHeartbeatHandler extends ChannelDuplexHandler {
    private final static Logger LOGGER = LoggerFactory.getLogger(CheckHeartbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            SocketAddress socketAddress = ctx.channel().remoteAddress();
            IdleState idleState = idleStateEvent.state();
            switch (idleState) {
                case READER_IDLE:
                case ALL_IDLE:
                    LOGGER.info("triggered【{}】 close idle channel remoteAddress【{}】", idleState, socketAddress);
                    ctx.close();
                    break;
                case WRITER_IDLE:
                    ctx.writeAndFlush("ping").addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                    LOGGER.info("triggered【{}】 and ping remoteAddress【{}】", idleState, socketAddress);
                    break;
                    default:
            }
        }
    }
}
