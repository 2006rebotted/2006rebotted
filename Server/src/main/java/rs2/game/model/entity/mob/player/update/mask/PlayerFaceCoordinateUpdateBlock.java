package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.Position;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players facing
 * direction.
 * 
 * @author SeVen
 */
public class PlayerFaceCoordinateUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerFaceCoordinateUpdateBlock}.
	 */
	public PlayerFaceCoordinateUpdateBlock() {
		super(0x2, UpdateFlag.FACE_COORDINATE);
	}

	@Override
	public void encode(Player target, GamePacketBuilder builder) {
		final Position location = target.getFacingLocation();
		int x = location == null ? 0 : location.getX();
		int y = location == null ? 0 : location.getY();
		builder.writeShort(x * 2 + 1, ByteModification.ADDITION, ByteOrder.LITTLE)
		.writeShort(y * 2 + 1, ByteOrder.LITTLE);
	}

}
