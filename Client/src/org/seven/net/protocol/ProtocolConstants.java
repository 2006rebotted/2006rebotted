package org.seven.net.protocol;

/**
 * Class containing protocol-related constants.
 * @author SeVen
 */
public class ProtocolConstants {

	/**
	 * Signifies a connection to the game server.
	 */
	public static final int GAME_SEVER_OPCODE = 14;
	
	/**
	 * The algorithm to encrypt to the login block.
	 */
	public static final int LOGIN_BLOCK_ENCRYPTION_KEY = (0x24 + 0x1 + 0x1 + 0x2);
	
	/**
	 * The magic number.
	 */
	public static final int MAGIC_NUMBER_OPCODE = 0xFF;
	
	/**
	 * Signifies a new connection.
	 */
	public static final int NEW_CONNECTION_OPCODE = 16;
	
	/**
	 * Signifies the return of an existing connection.
	 */
	public static final int RECONNECTION_OPCODE = 18;
	
	/**
	 * Signifies the client's protocol revision.
	 */
	public static final int PROTOCOL_REVISION = 317;

}
