package astraeus.net.packet.in;

import astraeus.game.model.World;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.npc.NpcDefinition;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.Receivable;
import astraeus.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode({IncomingPacket.MAGIC_ON_NPC})
public final class MagicOnNpcPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
        final int slot = packet.getReader().readShort(ByteOrder.LITTLE, ByteModification.ADDITION);
        final Npc mobMagic = World.world.getMobs().get(slot);
        @SuppressWarnings("unused")
        final int spell = packet.getReader().readShort(ByteModification.ADDITION);

        if (mobMagic == null) {
              player.queuePacket(new ServerMessagePacket("You tried to attack a mob that doesn't exist."));
              return;
        }

        NpcDefinition def = NpcDefinition.get(mobMagic.getId());

        if (def == null) {
              return;
        }
	}

}
