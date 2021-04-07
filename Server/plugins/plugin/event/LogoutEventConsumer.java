package plugin.event;

import astraeus.game.event.EventContext;
import astraeus.game.event.EventSubscriber;
import astraeus.game.event.SubscribesTo;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.event.LogoutEvent;
import astraeus.net.packet.out.LogoutPlayerPacket;

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
