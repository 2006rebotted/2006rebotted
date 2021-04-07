package astraeus.net.packet;

import io.netty.buffer.ByteBuf;

/**
 * Represents an outgoing packet that can be sent to the client.
 * 
 * @author Vult-R
 */
public final class OutgoingPacket {

	/**
	 * The opcode of this packet.
	 */
	private final int opcode;

	/**
	 * The header for this packet.
	 */
	private final PacketHeader header;

	/**
	 * The payload of this packet.
	 */
	private final ByteBuf payload;

	/**
	 * Creates a new {@link OutgoingPacket}.
	 *
	 * @param opcode
	 * 		The id of this packet.
	 *
	 * @param header
	 * 		The header for this packet.
	 */
	public OutgoingPacket(int opcode, PacketHeader header, ByteBuf payload) {
		this.opcode = opcode;
		this.header = header;
		this.payload = payload;
	}

	/**
	 * Gets the header of this packet.
	 *
	 * @return The header.
	 */
	public PacketHeader getHeader() {
		return header;
	}

	/**
	 * Gets the opcode of this packet.
	 *
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}

	/**
	 * The buffer that contains the data for this packet.
	 *
	 * @retunr The payload.
	 */
	public ByteBuf getPayload() {
		return payload;
	}

	/**
	 * Gets the size of this packet.
	 *
	 * @return The packet size.
	 */
	public int getSize() {
		return payload.readableBytes();
	}

}
