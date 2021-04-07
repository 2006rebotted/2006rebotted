package astraeus.net.packet;

/**
 * Represents the header of a packet.
 * 
 * @author Vult-R
 */
public enum PacketHeader {

    /**
     * No packet header is to be placed on the message.
     */
    EMPTY,

    /**
     * A fixed size packet where the size never changes.
     */
    FIXED,

    /**
     * A variable packet where the size is described by a byte.
     */
    VARIABLE_BYTE,

    /**
     * A variable packet where the size is described by a short or a word.
     */
    VARIABLE_SHORT;

}
