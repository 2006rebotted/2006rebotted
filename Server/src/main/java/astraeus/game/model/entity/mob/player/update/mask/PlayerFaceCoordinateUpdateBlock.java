package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

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
