package plugin.event;

import java.util.logging.Logger;

import astraeus.game.event.EventContext;
import astraeus.game.event.EventSubscriber;
import astraeus.game.event.SubscribesTo;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.attr.AttributeKey;
import astraeus.game.model.entity.mob.player.event.PostLoginEvent;
import astraeus.game.model.entity.mob.player.event.RegisterPlayerEvent;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.util.LoggerUtils;

@SubscribesTo(RegisterPlayerEvent.class)
public final class RegisterPlayerEventConsumer implements EventSubscriber<RegisterPlayerEvent> {
	
	private static final Logger logger = LoggerUtils.getLogger(RegisterPlayerEventConsumer.class);

	@Override
	public void subscribe(EventContext context, Player player, RegisterPlayerEvent event) {
		World.world.register(event.getPlayer());
		event.getPlayer().setRegionChange(true);
		event.getPlayer().getUpdateFlags().add(UpdateFlag.APPEARANCE);		
		event.getPlayer().setPosition(event.getPlayer().attr().contains(AttributeKey.valueOf("new_player", true)) ? Player.defaultSpawn : event.getPlayer().getPosition());
		logger.info(String.format("[REGISTERED]: [user= %s]", event.getPlayer().getUsername()));
		
		player.post(new PostLoginEvent(event.getPlayer()));
	}

}
