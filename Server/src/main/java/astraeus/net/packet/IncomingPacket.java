package astraeus.net.packet;

import io.netty.buffer.ByteBuf;
import astraeus.net.codec.game.ByteBufReader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Represents a single packet received from the client.
 * 
 * @author Seven
 */
public final class IncomingPacket {

	/**
	 * The reflection will be available for reference at run-time.
	 */
	@Retention(RetentionPolicy.RUNTIME)

	/**
	 * A class, enumeration, or interface will be annotated.
	 */
	@Target(ElementType.TYPE)

	public @interface IncomingPacketOpcode {

		/**
		 * The incoming opcodes the packet implementation handles.
		 * 
		 * @return The collection of opcodes.
		 */
		int[] value();
	}

	/**
	 * Sent when a player adds another player to their friends list.
	 */
	public static final int ADD_FRIEND = 188;

	/**
	 * Sent when a player adds another player to their ignore list.
	 */
	public static final int ADD_IGNORE = 133;

	/**
	 * Sent when a player uses the attack right-click option on a mob
	 */
	public static final int ATTACK_NPC = 72;

	/**
	 * Sent when a player requests to bank an X amount of items.
	 */
	public static final int BANK_X_PART_1 = 135;

	/**
	 * Sent when a player enters an X amount of items they want to bank.
	 */
	public static final int BANK_X_PART_2 = 208;

	/**
	 * Sent when a player clicks an in-game button.
	 */
	public static final int BUTTON_CLICK = 185;

	/**
	 * Sent when a player moves the camera.
	 */
	public static final int CAMERA_MOVEMENT = 86;

	/**
	 * Sent when the player enters a chat message.
	 */
	public static final int CHAT = 4;

	/**
	 * Sent when a player tries to change their appearance by using the character design interface.
	 */
	public static final int CHARACTER_DESIGN = 101;

	/**
	 * Sent when a player presses the close, exit or cancel button on an
	 * interface.
	 */
	public static final int CLOSE_WINDOW = 130;

	/**
	 * Sent when a player drops an item.
	 */
	public static final int DROP_ITEM = 87;

	/**
	 * Sent when a player enters a new map region.
	 */
	public static final int ENTER_REGION = 210;

	/**
	 * Sent when a player tries to equip an item.
	 */
	public static final int EQUIP_ITEM = 41;

	/**
	 * Sent when a player uses the first option of an object.
	 */
	public static final int OBJECT_OPTION_1 = 132;

	/**
	 * Sent when a player uses the second option of an object.
	 */
	public static final int OBJECT_OPTION_2 = 252;

	/**
	 * Sent when a player uses the third option of an object.
	 */
	public static final int OBJECT_OPTION_3 = 70;
	
	/**
	 * Sent when a player uses the fourth option of an object.
	 */
	public static final int OBJECT_OPTION_4 = 234;
	
	/**
	 * Sent when a player uses the fifth option of an object.
	 */
	public static final int OBJECT_OPTION_5 = 228;

	/**
	 * Sent when the game client window goes in and out of focus.
	 */
	public static final int FOCUS_CHANGE = 3;

	/**
	 * Sent when a player remains idle for 5 minutes.
	 */
	public static final int IDLE_LOGOUT = 202;
	
	/**
	 * Sent when a player uses the first option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_1 = 145;
	
	/**
	 * Sent when a player uses the second option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_2 = 117;
	
	/**
	 * Sent when a player uses the third option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_3 = 43;
	
	/**
	 * Sent when a player uses the fourth option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_4 = 129;
	
	/**
	 * Sent when a player uses the fifth option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_5 = 135;
	
	/**
	 * Sent when a player uses the sixth option of a container type widget.
	 */
	public static final int WIDGET_CONTAINER_OPTION_6 = 208;

	/**
	 * Sent when a player uses an item on another item thats on the floor.
	 */
	public static final int ITEM_ON_GROUND_ITEM = 25;

	/**
	 * Sent when a player uses an item on a scene object.
	 */
	public static final int ITEM_ON_OBJECT = 192;

	/**
	 * Sent when a player uses an item on another player.
	 */
	public static final int ITEM_ON_PLAYER = 14;

	/**
	 * Sent when a player uses an item on another item.
	 */
	public static final int ITEM_ON_ITEM = 53;

	/**
	 * Send when a player uses an item on an in-game npc.
	 */
	public static final int ITEM_ON_NPC = 57;
	
	/**
	 * Send when a player uses the first option of an item.
	 */
	public static final int ITEM_OPTION_1 = 122;
	
	/**
	 * Send when a player uses the second option of an item.
	 */
	public static final int ITEM_OPTION_2 = 16;	
	
	/**
	 * Send when a player uses the third option of an item.
	 */
	public static final int ITEM_OPTION_3 = 75;

	/**
	 * Sent when the client finishes loading a map region.
	 */
	public static final int LOADED_REGION = 121;

	/**
	 * Sent when a player casts magic on the items in their inventory.
	 */
	public static final int MAGIC_ON_ITEMS = 237;

	/**
	 * Sent when a player uses magic on an npc.
	 */
	public static final int MAGIC_ON_NPC = 131;

	/**
	 * Sent when a player attempts to cast magic on another player.
	 */
	public static final int MAGIC_ON_PLAYER = 249;
	
	/**
	 * Sent when a player uses a spell on a ground item.
	 */
	public static final int MAGIC_ON_GROUND_ITEM = 181;

	/**
	 * Sent when a player moves an item from one slot to another.
	 */
	public static final int MOVE_ITEM = 214;

	/**
	 * Sent when the player walks using the map. Has 14 additional (assumed to
	 * be anticheat) bytes added to the end of it that are ignored.
	 */
	public static final int MAP_WALK = 248;

	/**
	 * Sent when a player uses the first right-click option on an npc.
	 */
	public static final int NPC_OPTION_1 = 155;

	/**
	 * Sent when a player uses the second right-click option on an npc.
	 */
	public static final int NPC_OPTION_2 = 17;

	/**
	 * Sent when a player uses the third right-click option on an npc.
	 */
	public static final int NPC_OPTION_3 = 21;

	/**
	 * Sent when the player picks up an item from the ground.
	 */
	public static final int PICKUP_GROUND_ITEM = 236;

	/**
	 * Sent when a player enters a player command.
	 */
	public static final int PLAYER_COMMAND = 103;

	/**
	 * Sent when a player changes their privacy options (i.e. public chat).
	 */
	public static final int PRIVACY_OPTIONS = 95;
	
	/**
	 * Sent when a player uses the first option of a player. (challenge)
	 */
	public static final int PLAYER_OPTION_1 = 128;
	
	/**
	 * Sent when a player uses the second option of a player. (attack)
	 */
	public static final int PLAYER_OPTION_2 = 73;
	
	/**
	 * Sent when a player uses the third option of a player. (follow)
	 */
	public static final int PLAYER_OPTION_3 = 139;
	
	/**
	 * Sent when a player uses the fourth option of a player. (trade)
	 */
	public static final int PLAYER_OPTION_4 = 39;

	/**
	 * Sent when a player sends another player a private message.
	 */
	public static final int PRIVATE_MESSAGE = 126;

	/**
	 * Sent when the player walks regularly.
	 */
	public static final int REGULAR_WALK = 164;

	/**
	 * Sent when a player removes another player from their friends list.
	 */
	public static final int REMOVE_FRIEND = 215;

	/**
	 * Sent when a player removes an ignored user from the ignore list.
	 */
	public static final int REMOVE_IGNORE = 59;
	
	/**
	 * Sent when a player reports another player.
	 */
	public static final int REPORT_PLAYER = 218;

	/**
	 * Sent when a player answers a trade request from another player.
	 */
	public static final int TRADE_ANSWER = 153;

	/**
	 * Sent while typing onto a widget.
	 */
	public static final int TYPE_ON_WIDGET = 60;

	/**
	 * Sent when the player should walk somewhere according to a certain action
	 * performed, such as clicking an object.
	 */
	public static final int WALK_ON_COMMAND = 98;

	/**
	 * The opcode for this packet.
	 */
	private final int opcode;

	/**
	 * The header for this packet.
	 */
	private final PacketHeader header;

	/**
	 * The reader used to read the packet information.
	 */
	private final ByteBufReader reader;
	
	/**
	 * The size of this packet.
	 */
	private final int size;

	/**
	 * Creates a {@link IncomingPacket}.
	 * 
	 * @param opcode
	 *            The opcode.
	 * @param header
	 *            The header.
	 * @param payload
	 *            The payload.
	 */
	public IncomingPacket(final int opcode, final PacketHeader header, final ByteBuf payload) {
		this.opcode = opcode;
		this.header = header;		
		this.reader = new ByteBufReader(payload);
		size = payload.readableBytes();
	}
	
	public int getLength() {
		return reader.getPayload().capacity();
	}
	
	/**
	 * Gets the size of this payload.
	 * 
	 * @return The amount of readable bytes.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Gets the opcode.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Gets the type.
	 * 
	 * @return The type.
	 */
	public PacketHeader getHeader() {
		return header;		
	}
	
	/**
	 * Gets the payload.
	 * 
	 * @return The payload.
	 */
	public ByteBuf getPayload() {
		return reader.getPayload();
	}
	
	/**
	 * Gets the reader for this packet.
	 * 
	 * @return The reader.
	 */
	public ByteBufReader getReader() {
		return reader;
	}

	/**
	 * Determines if this packet is prioritized.
	 * 
	 * @return {@code true} If this packet is prioritized. {@code false}
	 *         otherwise.
	 */
	public boolean isPrioritized() {
		return opcode == IncomingPacket.EQUIP_ITEM || opcode == IncomingPacket.ITEM_OPTION_1 || opcode == IncomingPacket.BUTTON_CLICK;
	}

	@Override
	public String toString() {
		return String.format("[opcode= %d], [header= %s], [size= %d], [length= %d]", opcode, header.name(), size, getLength());
	}

}
