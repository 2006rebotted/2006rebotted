package astraeus.net.codec.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * The class that will encode the login response and send it to the client.
 * 
 * @author Vult-R
 */
public final class LoginResponseEncoder extends MessageToByteEncoder<LoginResponsePacket> {

	@Override
	protected void encode(ChannelHandlerContext ctx, LoginResponsePacket msg, ByteBuf out) throws Exception {

		out.writeByte(msg.getResponse().getOpcode());

		if (msg.getResponse() == LoginResponse.NORMAL) {
			out.writeByte(msg.getRights().getProtocolValue());
			out.writeByte(msg.isFlagged() ? 1 : 0);
		}
	}

}
