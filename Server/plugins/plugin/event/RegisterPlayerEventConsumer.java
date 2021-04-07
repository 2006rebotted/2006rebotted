package plugin.event;

import java.util.logging.Logger;

import rs2.game.event.EventContext;
import rs2.game.event.EventSubscriber;
import rs2.game.event.SubscribesTo;
import rs2.game.model.World;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.attr.AttributeKey;
import rs2.game.model.entity.mob.player.event.PostLoginEvent;
import rs2.game.model.entity.mob.player.event.RegisterPlayerEvent;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.util.LoggerUtils;

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
