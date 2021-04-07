package astraeus.net.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import astraeus.net.NetworkConstants;
import astraeus.net.codec.login.LoginRequestDecoder;
import astraeus.net.codec.login.LoginResponseEncoder;

/**
 * The {@link ChannelInitializer} implementation that will setup a channel's {@link ChannelPipeline}.
 *
 * @author Seven
 */
@Sharable
public class ChannelPiplineInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * The part of the pipeline that limits connections, and checks for any banned hosts.
     */
    private final ChannelFilter FILTER = new ChannelFilter();

    /**
     * The part of the pipeline that handles exceptions caught, channels being read, in-active channels, and channel triggered events.
     */
    private final UpstreamChannelHandler HANDLER = new UpstreamChannelHandler();
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
          final ChannelPipeline pipeline = ch.pipeline();
          
          ch.attr(NetworkConstants.SESSION_KEY).setIfAbsent(new PlayerChannel(ch));

          pipeline.addLast("channel-filter", FILTER);
          pipeline.addLast("login-request-decoder", new LoginRequestDecoder());
          pipeline.addLast("login-encoder", new LoginResponseEncoder());
          pipeline.addLast("timeout", new IdleStateHandler(NetworkConstants.INPUT_TIMEOUT, 0, 0));
          pipeline.addLast("channel-handler", HANDLER);
    }
    
}
