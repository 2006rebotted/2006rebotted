package plugin.shops;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import rs2.game.GameConstants;
import rs2.game.model.World;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.item.ItemContainer;
import rs2.game.model.entity.item.ItemContainerPolicy;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.ServerMessagePacket;
import rs2.net.packet.out.SetWidgetStringPacket;
import rs2.net.packet.out.UpdateItemsOnWidgetPacket;
import rs2.util.StringUtils;

/**
 * The container that represents a shop players can buy and sell items from.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Shop {

	/**
	 * The name of this current shop.
	 */
	private final String name;

	/**
	 * The flag that determines if this shop will restock its items.
	 */
	private final boolean restock;

	/**
	 * The flag that determines if items can be sold to this shop.
	 */
	private final boolean canSell;

	/**
	 * The currency that items within this shop will be bought with.
	 */
	private final Currency currency;

	/**
	 * The item container that contains the items within this shop.
	 */
	private final ItemContainer container = new ItemContainer(40, ItemContainerPolicy.STACK_ALWAYS);

	/**
	 * The map of cached shop item identifications and their amounts.
	 */
	private final Map<Integer, Integer> itemCache;

	/**
	 * The set of players that are currently viewing this shop.
	 */
	private final Set<Player> players = new HashSet<>();

	/**
	 * The shop restock task that will restock the shops.
	 */
	private ShopRestockTask restockTask;

	/**
	 * Creates a new {@link Shop}.
	 *
	 * @param name
	 *            the name of this current shop.
	 * @param items
	 *            the items within this shop.
	 * @param restock
	 *            the flag that determines if this shop will restock its items.
	 * @param canSell
	 *            the flag that determines if items can be sold to this shop.
	 * @param currency
	 *            the currency that items within this shop will be bought with.
	 */
	public Shop(String name, Item[] items, boolean restock, boolean canSell, Currency currency) {
		this.name = name;
		this.restock = restock;
		this.canSell = canSell;
		this.currency = currency;
		this.container.setItems(items);
		this.itemCache = new HashMap<>(container.capacity());
		Arrays.stream(items).filter(Objects::nonNull).forEach(item -> itemCache.put(item.getId(), item.getAmount()));
	}

	/**
	 * Opens this shop by displaying the interface for {@code player}.
	 *
	 * @param player
	 *            the player to open the shop for.
	 */
	public void open(Player player) {
		player.queuePacket(new UpdateItemsOnWidgetPacket(3823, player.getInventory().container()));
		player.queuePacket(new UpdateItemsOnWidgetPacket(3900, container.container()));
		player.getWidgets().openInventoryWidget(3824, 3822);
		player.queuePacket(new SetWidgetStringPacket(name, 3901));
		player.attr().put(Player.SHOPPING_KEY, true);

		players.add(player);
	}

	/**
	 * Updates the items and the containers that display items for
	 * {@code player}.
	 *
	 * @param player
	 *            the player this shop will be updated for.
	 * @param checkStock
	 *            if the stock should be checked.
	 */
	public void update(Player player, boolean checkStock) {
		player.queuePacket(new UpdateItemsOnWidgetPacket(3823, player.getInventory().container()));
		players.forEach(p -> player.queuePacket(new UpdateItemsOnWidgetPacket(3900, container.container())));

		if (checkStock && restock) {
			if (restockTask != null && !restockTask.hasStopped()) {
				return;
			}

			if (!needsRestock()) {
				return;
			}

			restockTask = new ShopRestockTask(this);
			World.world.submit(restockTask);
		}
	}

	/**
	 * Sends the determined selling value of {@code item} to {@code player}.
	 *
	 * @param player
	 *            the player to send the value to.
	 * @param item
	 *            the item to send the value of.
	 */
	public void sendSellingPrice(Player player, Item item) {
		String itemName = item.definition().getName();

		if (!canSell) {
			player.queuePacket(new ServerMessagePacket("You cannot sell any items to this store."));
			return;
		}
		if (GameConstants.INVALID_SHOP_ITEMS.stream().anyMatch(i -> i == item.getId())) {
			player.queuePacket(new ServerMessagePacket("You can't sell " + itemName + " " + "here."));
			return;
		}
		if (!container.contains(item.getId()) && !name.equalsIgnoreCase("General Store")) {
			player.queuePacket(new ServerMessagePacket("You can't sell " + itemName + " " + "here."));
			return;
		}
		String formatPrice = StringUtils.formatPrice((int) Math.floor(determinePrice(item) / 2));
		player.queuePacket(
				new ServerMessagePacket(itemName + ": shop will buy for " + formatPrice + " " + currency + "."));
	}

	/**
	 * Sends the determined purchase value of {@code item} to {@code player}.
	 *
	 * @param player
	 *            the player to send the value to.
	 * @param item
	 *            the item to send the value of.
	 */
	public void sendPurchasePrice(Player player, Item item) {
		Item shopItem = container.searchItem(item.getId()).orElse(null);
		if (shopItem == null)
			return;
		if (shopItem.getAmount() <= 0) {
			player.queuePacket(new ServerMessagePacket("There is none of this item left in stock!"));
			return;
		}
		player.queuePacket(new ServerMessagePacket(item.definition().getName() + ": " + "shop will sell for "
				+ StringUtils.formatPrice(determinePrice(item)) + " " + currency + "."));
	}

	/**
	 * The method that allows {@code player} to purchase {@code item}.
	 *
	 * @param player
	 *            the player who will purchase this item.
	 * @param item
	 *            the item that will be purchased.
	 * @return {@code true} if the player purchased the item, {@code false}
	 *         otherwise.
	 */
	public boolean purchase(Player player, Item item) {
		Optional<Item> optional = container.searchItem(item.getId());

		if (!optional.isPresent()) {
			return false;
		}

		Item shopItem = optional.get();

		if (shopItem.getAmount() <= 0) {
			player.queuePacket(new ServerMessagePacket("There is none of this item left in stock!"));
			return false;
		}
		if (item.getAmount() > shopItem.getAmount())
			item.setAmount(shopItem.getAmount());
		if (!player.getInventory().spaceFor(item)) {
			item.setAmount(player.getInventory().remaining());

			if (item.getAmount() == 0) {
				player.queuePacket(
						new ServerMessagePacket("You do not have enough space in your inventory to buy this item!"));
				return false;
			}
		}

		// TODO special shop values
		int value = currency == Currency.COINS ? item.definition().getShopValue() : item.definition().getShopValue();
		if (!(currency.getCurrency().currencyAmount(player) >= (value * item.getAmount()))) {
			player.queuePacket(new ServerMessagePacket("You do not have enough " + currency + " to buy this item."));
			return false;
		}
		if (player.getInventory().remaining() >= item.getAmount() && !item.definition().isStackable()
				|| player.getInventory().remaining() >= 1 && item.definition().isStackable()
				|| player.getInventory().contains(item.getId()) && item.definition().isStackable()) {

			if (itemCache.containsKey(item.getId())) {
				container.searchItem(item.getId()).ifPresent(i -> i.remove(item.getAmount()));
			} else if (!itemCache.containsKey(item.getId())) {
				container.remove(item);
			}
			currency.getCurrency().takeCurrency(player, item.getAmount() * value);
			player.getInventory().add(item);
		} else {
			player.queuePacket(new ServerMessagePacket("You don't have enough space in " + "your inventory."));
			return false;
		}
		update(player, true);
		return true;
	}

	/**
	 * The method that allows {@code player} to sell {@code item}.
	 *
	 * @param player
	 *            the player who will sell this item.
	 * @param item
	 *            the item that will be sold.
	 * @return {@code true} if the player sold the item, {@code false}
	 *         otherwise.
	 */
	public boolean sell(Player player, Item item, int fromSlot) {
		if (!Item.valid(item))
			return false;

		if (!canSell) {
			player.queuePacket(new ServerMessagePacket("You cannot sell items here."));
			return false;
		}

		if (GameConstants.INVALID_SHOP_ITEMS.stream().anyMatch(i -> i == item.getId())) {
			player.queuePacket(new ServerMessagePacket("You can't sell " + item.definition().getName() + " here."));
			return false;
		}

		if (!player.getInventory().contains(item.getId())) {
			return false;
		}

		if (!container.contains(item.getId()) && !name.equalsIgnoreCase("General Store")) {
			player.queuePacket(
					new ServerMessagePacket("You can't sell " + item.definition().getName() + " to this store."));
			return false;
		}

		if (!container.spaceFor(item)) {
			player.queuePacket(new ServerMessagePacket(
					"There is no room in this store " + "for the item you are trying to sell!"));
			return false;
		}

		if (player.getInventory().remaining() == 0 && !currency.getCurrency().canRecieveCurrency(player)) {
			player.queuePacket(
					new ServerMessagePacket("You do not have enough space in " + "your inventory to sell this item!"));
			return false;
		}

		int amount = player.getInventory().amount(item.getId());
		if (item.getAmount() > amount && !item.definition().isStackable()) {
			item.setAmount(amount);
		} else if (item.getAmount() > player.getInventory().get(fromSlot).getAmount()
				&& item.definition().isStackable()) {
			item.setAmount(player.getInventory().get(fromSlot).getAmount());
		}

		player.getInventory().remove(item);

		currency.getCurrency().recieveCurrency(player, item.getAmount() * (int) Math.floor(determinePrice(item) / 2));

		if (container.contains(item.getId())) {
			container.searchItem(item.getId()).ifPresent(i -> i.add(item.getAmount()));
		} else {
			container.add(item);
		}
		update(player, false);
		return true;
	}

	/**
	 * Determines the price of {@code item} based on the currency.
	 *
	 * @param item
	 *            the item to determine the price of.
	 * @return the price of the item based on the currency.
	 */
	// TODO add a special value to item definitions
	private int determinePrice(Item item) {
		return currency == Currency.COINS ? item.definition().getShopValue() : item.definition().getShopValue();
	}

	/**
	 * Determines if the items in the container no longer need to be restocked.
	 *
	 * @return {@code true} if the items don't to be restocked, {@code false}
	 *         otherwise.
	 */
	protected boolean restockCompleted() {
		return container.stream().filter(Objects::nonNull)
				.allMatch(i -> itemCache.containsKey(i.getId()) && i.getAmount() >= itemCache.get(i.getId()));
	}

	/**
	 * Determines if the items in the container need to be restocked.
	 *
	 * @return {@code true} if the items need to be restocked, {@code false}
	 *         otherwise.
	 */
	protected boolean needsRestock() {
		return container.stream().filter(Objects::nonNull)
				.anyMatch(i -> i.getAmount() <= 0 && itemCache.containsKey(i.getId()));
	}

	/**
	 * Gets the name of this current shop.
	 *
	 * @return the name of this shop.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Determines if this shop will restock its items.
	 *
	 * @return {@code true} if this shop will restock, {@code false} otherwise.
	 */
	public boolean isRestock() {
		return restock;
	}

	/**
	 * Determines if items can be sold to this shop.
	 *
	 * @return {@code true} if items can be sold, {@code false} otherwise.
	 */
	public boolean isCanSell() {
		return canSell;
	}

	/**
	 * Gets the currency that items within this shop will be bought with.
	 *
	 * @return the currency that items will be bought with.
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Gets an unmodifiable version of the map of cached shop item
	 * identifications and their amounts.
	 *
	 * @return the map of cached shop items.
	 */
	public Map<Integer, Integer> getItemCache() {
		return itemCache;
	}

	/**
	 * Gets the the set of players that are currently viewing this shop.
	 *
	 * @return the set of players viewing the shop.
	 */
	public Set<Player> getPlayers() {
		return players;
	}

	/**
	 * Gets the item container that contains the items within this shop.
	 *
	 * @return the container that contains the items.
	 */
	public ItemContainer getContainer() {
		return container;
	}

}
