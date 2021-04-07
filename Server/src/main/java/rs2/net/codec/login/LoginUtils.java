package rs2.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

/**
 * The class that contains login-related methods.
 * 
 * @author Vult-R
 */
public final class LoginUtils {
	
	/**
	 * The private constructor to prevent instantiation.
	 */
	private LoginUtils() {
		
	}
	
	/**
	 * Sends a response code to the client to notify the user logging in.
	 * 
	 * @param ctx
	 *            The context of the channel handler.
	 * 
	 * @param response
	 *            The response code to send.
	 */
	public static void sendResponseCode(ChannelHandlerContext ctx, LoginResponse response) {
		ByteBuf buffer = Unpooled.buffer(1);
		buffer.writeByte(response.getOpcode());
		ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
	}

}
