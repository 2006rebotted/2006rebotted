package rs2.net.packet.in;

import rs2.game.model.World;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.NpcDefinition;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.codec.ByteModification;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.ServerMessagePacket;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.ATTACK_NPC)
public final class AttackNpcPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
        final int npcIndex = packet.getReader().readShort(false, ByteModification.ADDITION);

        if (npcIndex < 0 || npcIndex > NpcDefinition.MOB_LIMIT) {
              return;
        }

        final Npc npc = World.world.getMobs().get(npcIndex);

        if (npc == null) {
              return;
        }

        if (player.getRights().greaterOrEqual(PlayerRights.DEVELOPER) && player.attr().get(Player.DEBUG_KEY)) {
              player.queuePacket(new ServerMessagePacket(String.format("[attack= npc], [id= %d], [slot= %d]", npc.getId(), npc.getSlot())));
        }

        if (npc.getCurrentHealth() <= 0) {
              player.queuePacket(new ServerMessagePacket("This npc is already dead..."));
              return;
        }
	}

}
