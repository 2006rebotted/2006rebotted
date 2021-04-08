package rs2.net.packet.out;

import rs2.game.model.Direction;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.object.GameObject;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that creates an object for a user in the game
 * world.
 * 
 * @author SeVen
 */
public final class AddObjectPacket implements Sendable {

  /**
   * The object to spawn.
   */
  private final GameObject object;

  /**
   * The flag that denotes this is not a door.
   */
  private final boolean normal;

  /**
   * Creates a new {@link AddObjectPacket}.
   * 
   * @param object
   *            The object to spawn.
   *
   *  @param normal
   *      The flag that denotes this is not a door.
   */
  public AddObjectPacket(GameObject object, boolean normal) {
    this.object = object;
    this.normal = normal;
  }

  @Override
  public Optional<OutgoingPacket> writePacket(Player player) {
    GamePacketBuilder builder = new GamePacketBuilder(151);
    player.queuePacket(new SetUpdateRegionPacket(object.getPosition()));
    builder.write(object.getPosition().getHeight(), ByteModification.ADDITION)
    .writeShort(object.getId(), ByteOrder.LITTLE)
    .write(object.getType() << 2 | (normal ? object.getOrientation() : Direction.getDoorOrientation(object.getEnumeratedOrientation()) & 3), ByteModification.SUBTRACTION);
    return builder.toOutgoingPacket();
  }

}
