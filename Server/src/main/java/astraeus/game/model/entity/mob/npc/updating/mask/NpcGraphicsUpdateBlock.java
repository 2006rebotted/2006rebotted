package astraeus.game.model.entity.mob.npc.updating.mask;

import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.game.GamePacketBuilder;

public final class NpcGraphicsUpdateBlock extends NpcUpdateBlock {

	public NpcGraphicsUpdateBlock() {
		super(0x80, UpdateFlag.GRAPHICS);
	}

	@Override
	public void encode(Npc entity, GamePacketBuilder builder) {
		builder.writeShort(entity.getGraphic().getId())
		.writeInt(entity.getGraphic().getHeight());
	}

}
