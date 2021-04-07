package rs2.game.model.entity.mob.npc.drop;

import java.util.HashMap;
import java.util.Map;

/**
 * A class which represents a single npc drop.
 * 
 * @author SeVen <https://github.com/7winds>
 */
public class NpcDrop {

    /**
     * The map of drops mapped to their npc ids.
     */
    private static Map<Integer, NpcDrop[]> MOB_DROPS = new HashMap<>();

    /**
     * The item id for the item being dropped.
     */
    private final int itemId;
    
    /**
     * The minimum quantity that can be dropped.
     */
    private int minAmount;
    
    /**
     * The maximum quantity that can be dropped.
     */
    private int maxAmount;

    /**
     * The chance for this drop.
     */
    private final DropChance chance;

    /**
     * A single npc drop
     * 
     * @param itemId
     *            the item id for the item being dropped.
     * @param chance
     *            the items chance for being dropped.
     * @param minAmount
     *            the minimum quantity for this drop.
     * @param maxAmount
     *            the maximum quantity for this drop.
     */
    public NpcDrop(int itemId, int minAmount, int maxAmount, DropChance chance) {
	this.itemId = itemId;
	this.minAmount = minAmount;
	this.maxAmount = maxAmount;
	this.chance = chance;
    }

    public static Map<Integer, NpcDrop[]> getMobDrops() {
	return MOB_DROPS;
    }    

    public static void setMobDrops(Map<Integer, NpcDrop[]> mobDrops) {
        MOB_DROPS = mobDrops;        
    }
    
    /**
     * Gets the drops for a specified npc.
     * 
     * @param id
     * 		The id of the npc to check.
     * 
     * @return The array of drops for this npc.
     */
    public static NpcDrop[] lookup(int id) {
	return MOB_DROPS.get(id);
    }    

    /**
     * @return the chance
     */
    public DropChance getChance() {
	return chance;
    }

    /**
     * Returns the difference between the minumum and maximum quantities.
     */
    public int getExtraAmount() {
	return maxAmount - minAmount;
    }

    /**
     * The item id of the item being dropped.
     */
    public int getItemId() {
	return itemId;
    }

    /**
     * Returns the max quantity that can be dropped for this item.
     */
    public int getMaxAmount() {
	return maxAmount;
    }

    /**
     * Returns the minimum quantity that can be dropped for this item.
     */
    public int getMinAmount() {
	return minAmount;
    }

    /**
     * Determines the item being dropped is rare based on the ordinal of the
     * chance.
     */
    public boolean isFromRareTable() {
	if (chance.ordinal() >= DropChance.RARE.ordinal()) {
	    return true;
	}
	return false;
    }

    /**
     * Sets the maximum quantity of an items drop.
     * 
     * @param amount
     *            the amount or quantity of the item being dropped.
     */
    public void setMaxAmount(int amount) {
	this.maxAmount = amount;
    }

    /**
     * Sets the minimum quantity of an items drop.
     * 
     * @param amount
     *            the amount or quantity of the item being dropped.
     */
    public void setMinAmount(int amount) {
	this.minAmount = amount;
    }
    
    @Override
    public String toString() {
	return "[DROP] - " + getItemId() + " " + getMinAmount() + " " + getMaxAmount() + " " + getChance().name();
    }

}
