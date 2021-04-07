package astraeus.net.codec;

/**
 * The class that contains protocol-related constants.
 * 
 * @author SeVen
 */
public final class ProtocolConstants {

    /**
     * Signifies a connection to the game server.
     */
    public static final int GAME_SEVER_OPCODE = 14;

    /**
     * Signifies a connection to the file service.
     */
    public static final int FILE_SERVER_OPCODE = 15;

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

    /**
     * The algorithm to encrypt to the login block.
     */
    public static final int LOGIN_BLOCK_ENCRYPTION_KEY = (0x24 + 0x1 + 0x1 + 0x2);

    /**
     * The minimum size a login block can hold.
     */
    public static final int LOGIN_BLOCK_SIZE_MINIMUM = 0x1;

    /**
     * The magic number.
     */
    public static final int MAGIC_NUMBER_OPCODE = 0xFF;
    
    public static final int ARCHIVE_COUNT = 0x9;

    /**
     * The amount of encryption keys to skip.
     */
    public static final int CRC_KEY_SKIPPING_AMOUNT = 0x9;

    /**
     * A constant collection of bit masks that are selected by the bit's
     * off-set.
     */
    public static final int[] BIT_MASK = { 0, 0x1, 0x3, 0x7, 0xf, 0x1f, 0x3f, 0x7f, 0xff, 0x1ff, 0x3ff, 0x7ff, 0xfff,
      0x1fff, 0x3fff, 0x7fff, 0xffff, 0x1ffff, 0x3ffff, 0x7ffff, 0xfffff, 0x1fffff, 0x3fffff, 0x7fffff, 0xffffff,
      0x1ffffff, 0x3ffffff, 0x7ffffff, 0xfffffff, 0x1fffffff, 0x3fffffff, 0x7fffffff, -1 };

    public static final int DEFAULT_OPCODE = -1;
    
    /**
     * The packet length of {@code 0} is said to be of {@code fixed}.
     */
    public static final int FIXED_BYTE = 0;

    /**
     * The packet length of {@code -1} is said to be of {@code variable byte}
     * length.
     */
    public static final int VARIABLE_BYTE = -1;

    /**
     * The packet length of {@code -2} is said to be of {@code variable short}
     * length.
     */
    public static final int VARIABLE_SHORT = -2;

}

