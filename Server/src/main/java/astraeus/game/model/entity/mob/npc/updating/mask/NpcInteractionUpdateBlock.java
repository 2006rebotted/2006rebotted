package astraeus.game.model.entity.mob.npc.updating.mask;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.game.GamePacketBuilder;

public class NpcInteractionUpdateBlock extends NpcUpdateBlock {

      public NpcInteractionUpdateBlock() {
            super(0x20, UpdateFlag.ENTITY_INTERACTION);
      }

      @Override
      public void encode(Npc mob, GamePacketBuilder builder) {
            final Mob entity = (Mob) mob.getInteractingEntity();
            builder.writeShort(entity == null ? -1 : (entity instanceof Player) ? entity.getSlot() + 32768 : entity.getSlot());
      }

}
