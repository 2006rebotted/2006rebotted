package astraeus.net.channel;

import com.google.common.base.Objects;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.NetworkConstants;

/**
 * A {@link ChannelInboundHandlerAdapter} implementation that explicitly handles {@link Object} type messages.
 *
 * @author Seven
 */
@Sharable
public final class UpstreamChannelHandler extends SimpleChannelInboundHandler<Object> {

      @Override
      protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    	  try {    		
    	        PlayerChannel session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
    	        
                  if (session == null) {
                        throw new IllegalStateException("session == null");
                  }
                  
                  session.handleIncomingPacket(msg);
            } catch (Exception ex) {
                  ex.printStackTrace();
            }
      }
      
      @Override
      public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            PlayerChannel session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
            if (session == null) {
              throw new IllegalStateException("session == null");
            }
            
            Player player = session.getPlayer();
            
            if (player == null) {
                  return;
            }
            
            World.world.queueLogout(player);
      }

      @Override
      public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                  IdleStateEvent event = (IdleStateEvent) evt;
                  if (event.state() == IdleState.READER_IDLE) {
                        ctx.channel().close();
                  }
            }
      }

      @Override
      public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
            if (!NetworkConstants.IGNORED_EXCEPTIONS.stream().anyMatch($it -> Objects.equal($it, e.getMessage()))) {
                  e.printStackTrace(); 
            }
            
            ctx.channel().close();
      }

}
