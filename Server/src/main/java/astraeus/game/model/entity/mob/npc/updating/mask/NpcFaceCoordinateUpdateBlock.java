package astraeus.game.model.entity.mob.npc.updating.mask;

import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

/**
 * The {@link NpcUpdateBlock} implementation that makes an npc face a certain direction.
 * 
 * @author SeVen
 */
public class NpcFaceCoordinateUpdateBlock extends NpcUpdateBlock {

	public NpcFaceCoordinateUpdateBlock() {
		super(0x2, UpdateFlag.FACE_COORDINATE);
	}

	@Override
	public void encode(Npc entity, GamePacketBuilder builder) {

		int x = 0;
		int y = 0;
		
		if (entity.getInteractingEntity() == null) {
			final Position location = entity.getPosition();
			x = location == null ? 0 : location.getX() + entity.getFacingDirection().getDirectionX();
			y = location == null ? 0 : location.getY()  + entity.getFacingDirection().getDirectionY();
		} else {
			final Position location = entity.getFacingLocation();
			x = location == null ? 0 : location.getX();
			y = location == null ? 0 : location.getY();
		}
		
		builder.writeShort(x * 2 + 1, ByteOrder.LITTLE)
		.writeShort(y * 2 + 1, ByteOrder.LITTLE);
	}

}
