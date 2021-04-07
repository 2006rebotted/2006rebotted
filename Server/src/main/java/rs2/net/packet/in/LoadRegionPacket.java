package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.attr.AttributeKey;
import rs2.game.model.entity.object.GameObjects;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;

@IncomingPacket.IncomingPacketOpcode(IncomingPacket.LOADED_REGION)
public final class LoadRegionPacket implements Receivable {

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {	
		
		player.attr().put(AttributeKey.valueOf("save", false), true);
		
		GameObjects.createGlobalObjects(player);
		// TODO this needs to be done better, load ground items for players entering regions
		//GameObjects.createGlobalItems(player);		
	}

}
