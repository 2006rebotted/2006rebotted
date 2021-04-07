package plugin.event;

import rs2.game.event.EventContext;
import rs2.game.event.EventSubscriber;
import rs2.game.event.SubscribesTo;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.event.LogoutEvent;
import rs2.net.packet.out.LogoutPlayerPacket;

@SubscribesTo(LogoutEvent.class)
public final class LogoutEventConsumer implements EventSubscriber<LogoutEvent> {

	@Override
	public void subscribe(EventContext context, Player player, LogoutEvent event) {		
		
		event.getPlayer().queuePacket(new LogoutPlayerPacket());
		
		event.getPlayer().resetEntityInteraction();
		
		event.getPlayer().attr().put(Player.ACTIVE_KEY, false);
		event.getPlayer().attr().put(Player.LOGOUT_KEY, true);
		event.getPlayer().attr().put(Player.DISCONNECTED_KEY, true);
	}

}
