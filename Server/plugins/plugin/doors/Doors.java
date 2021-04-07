package plugin.doors;

import java.util.ArrayList;
import java.util.List;

import astraeus.game.event.EventContext;
import astraeus.game.event.EventSubscriber;
import astraeus.game.event.SubscribesTo;
import astraeus.game.event.impl.DoorEvent;
import astraeus.game.model.entity.mob.player.Player;

@SubscribesTo(DoorEvent.class)
public final class Doors implements EventSubscriber<DoorEvent> {

	/**
	 * The list of door objects spawned in the game world.
	 */
	private static final List<Door> doors = new ArrayList<>();
	
	static {
		new DoorParser().run();
	}

	@Override
	public void subscribe(EventContext context, Player player, DoorEvent event) {
		DoorUtils.handleDoor(player, event.getDoor());		
	}
	
	public static List<Door> getDoors() {
		return doors;
	}

}
