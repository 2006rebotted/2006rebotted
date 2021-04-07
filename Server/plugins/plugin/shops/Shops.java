package plugin.shops;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import rs2.game.event.EventContext;
import rs2.game.event.EventSubscriber;
import rs2.game.event.SubscribesTo;
import rs2.game.model.entity.mob.player.Player;

@SubscribesTo(ShopEvent.class)
public final class Shops implements EventSubscriber<ShopEvent> {
	
	public static final Map<String, Shop> shops = new HashMap<>();
	
	static {
		new ShopParser().run();
	}	
	
	@Override
	public void subscribe(EventContext context, Player player, ShopEvent event) {
		shops.get(event.getName()).open(player);
	}
	
	public boolean test(ShopEvent event) {
		return shops.get(event.getName()) != null;
	}
	
	public static Optional<Shop> search(Player player) {
		return shops.values().stream().filter(it -> it.getPlayers().contains(player)).findFirst();
	}

}
