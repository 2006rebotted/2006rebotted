package astraeus.net.codec.login;

import java.util.List;
import java.util.logging.Logger;

import astraeus.net.codec.ProtocolConstants;
import astraeus.util.LoggerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * The class determines the type of connection.
 * 
 * 16 is a new connection.
 * 
 * 18 is reconnecting.
 * 
 * @author Vult-R
 */
public final class LoginTypeDecoder extends ByteToMessageDecoder {

	/**
	 * The single logger for this class.
	 */
	private static final Logger logger = LoggerUtils.getLogger(LoginTypeDecoder.class);	
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= 2) {
			int connectionType = in.readUnsignedByte();

			if (connectionType != ProtocolConstants.NEW_CONNECTION_OPCODE && connectionType != ProtocolConstants.RECONNECTION_OPCODE) {
				logger.info(String.format("[host= %s] was rejected for having the wrong connection type.", ctx.channel().remoteAddress()));
				LoginUtils.sendResponseCode(ctx, LoginResponse.LOGIN_SERVER_REJECTED_SESSION);
				return;
			}
			
			ctx.pipeline().replace("login-type-decoder", "login-decoder", new LoginDecoder());

		}
	}

}
