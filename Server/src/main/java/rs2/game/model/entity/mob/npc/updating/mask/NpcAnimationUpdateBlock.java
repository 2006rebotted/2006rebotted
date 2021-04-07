package rs2.game.model.entity.mob.npc.updating.mask;

import rs2.game.model.Animation;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;

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
