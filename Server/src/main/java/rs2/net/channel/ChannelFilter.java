package rs2.net.channel;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import rs2.game.model.World;
import rs2.net.NetworkConstants;
import rs2.net.codec.login.LoginResponse;
import rs2.net.codec.login.LoginResponsePacket;

import java.net.InetSocketAddress;

/**
 * The {@link ChannelInboundHandlerAdapter} implementation that will filter out unwanted connections from
 * propagating down the pipeline.
 *
 * @author Seven
 */
@Sharable
public class ChannelFilter extends ChannelInboundHandlerAdapter {

	/**
	 * The {@link Multiset} of connections currently active within the server.
	 */
	private final Multiset<String> connections = ConcurrentHashMultiset.create();

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		String host = getHost(ctx);

		// if this local then, do nothing and proceed to next handler in the pipeline.
		if (host.equalsIgnoreCase("127.0.0.1")) {
			return;
		}

		// add the host
		connections.add(host);

		// evaluate the amount of connections from this host.
		if (connections.count(host) > NetworkConstants.CONNECTION_LIMIT) {
			disconnect(ctx, LoginResponse.LOGIN_LIMIT_EXCEEDED);
			return;
		}
		// evaluate the host
		if (World.world.getIpBans().contains(host)) {
			disconnect(ctx, LoginResponse.ACCOUNT_DISABLED);
			return;
		}

		// Nothing went wrong, so register the channel and forward the event to next handler in the pipeline.
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		String host = getHost(ctx);

		// if this is local, do nothing and proceed to next handler in the pipeline.
		if (host.equalsIgnoreCase("127.0.0.1")) {
			return;
		}

		// remove the host from the connection list
		connections.remove(host);

		// the connection is unregistered so forward the event to the next handler in the pipeline.
		ctx.fireChannelUnregistered();
	}
	/**
	* Sends a {@code response} back to the user logging in indicating something went wrong.
	 * Then prepares to disconnect the user from the server.
	 *
	 * @param ctx
	 * 		The context of this channel.
	 *
	 * 	@param response
	 * 		The response code that is being sent.
	*/
	private void disconnect(ChannelHandlerContext ctx, LoginResponse response) {
		LoginResponsePacket message = new LoginResponsePacket(response);
		ByteBuf initialMessage = ctx.alloc().buffer(8).writeLong(0);

		ctx.channel().write(initialMessage, ctx.channel().voidPromise());
		ctx.channel().writeAndFlush(message).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * Gets the host address of the user logging in.
	 *
	 * @param ctx
	 * 		The context of this channel.
	 *
	 * 	@return The host address of this connection.
	 */
	public String getHost(ChannelHandlerContext ctx) {
		return ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
	}

}
