package rs2.net.packet.in;

import rs2.game.model.World;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.NpcDefinition;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;

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
