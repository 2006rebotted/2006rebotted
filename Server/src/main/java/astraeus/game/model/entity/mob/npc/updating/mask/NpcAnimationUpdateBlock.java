package astraeus.game.model.entity.mob.npc.updating.mask;

import astraeus.game.model.Animation;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

public class NpcAnimationUpdateBlock extends NpcUpdateBlock {

	public NpcAnimationUpdateBlock() {
		super(0x10, UpdateFlag.ANIMATION);
	}

	@Override
	public void encode(Npc entity, GamePacketBuilder builder) {
		final Animation animation = entity.getAnimation();
		builder.writeShort(animation.getId(), ByteOrder.LITTLE)
		.write(animation.getDelay());
	}

}
