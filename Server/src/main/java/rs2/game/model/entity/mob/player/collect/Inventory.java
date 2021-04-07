package rs2.game.model.entity.mob.player.collect;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.item.ItemContainer;
import rs2.game.model.entity.item.ItemContainerPolicy;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.ServerMessagePacket;

/**
 * The container that manages the inventory for a player.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class Inventory extends ItemContainer {

    /**
     * The player who's inventory is being managed.
     */
    private final Player player;

    /**
     * Create a new {@link Inventory}.
     *
     * @param player
     *            the player who's inventory is being managed.
     */
    public Inventory(Player player) {
        super(28, ItemContainerPolicy.NORMAL);
        this.player = player;
    }

    /**
     * Refreshes the contents of this inventory container to the interface.
     */
    public void refresh() {
        refresh(player, 3214);
    }

    @Override
    public boolean add(Item item, int slot) {
        if (!Item.valid(item))
            return false;
        if (!contains(item.getId()) && !item.definition().isStackable()) {
            if (freeSlot() == -1) {
                player.queuePacket(new ServerMessagePacket("You don't have enough space in your inventory!"));
                return false;
            }
        }
        boolean val = super.add(item, slot);
        refresh();
        return val;
    }

    @Override
    public boolean add(Item item) {
        return add(item, -1);
    }

    @Override
    public boolean remove(Item item, int slot) {
        boolean val = super.remove(item, slot);
        refresh();
        return val;
    }

    @Override
    public boolean remove(Item item) {
        return remove(item, -1);
    }
}