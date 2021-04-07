package rs2.game.model.entity.mob.npc.updating.mask;

import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.game.GamePacketBuilder;

public class NpcForceChatUpdateBlock extends NpcUpdateBlock {

	public NpcForceChatUpdateBlock() {
		super(0x1, UpdateFlag.FORCED_CHAT);
	}

	@Override
	public void encode(Npc entity, GamePacketBuilder builder) {
		builder.writeString(entity.getForcedChat());
	}

}
