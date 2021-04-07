package rs2.game.model.entity.item;

import rs2.game.model.entity.Entity;
import rs2.game.model.entity.EntityType;

import java.util.Objects;

/**
 * Represents a single in-game item that a player can obtain.
 * 
 * @author SeVen
 */
public final class Item extends Entity {

      /**
       * The id of this item.
       */
      private final int id;

      /**
       * The amount of this item.
       */
      private int amount;

      /**
       * Creates a new {@link Item}.
       * 
       * @param id
       *    The id of this item.
       */
      public Item(int id) {
            this(id, 1);
      }

      /**
       * The item a player can obtain.
       * 
       * @param id the id of the item.
       * @param quantity the quantity of the item.
       */
      public Item(int id, int quantity) {
            this.id = id;
            this.amount = quantity;
      }

      /**
       * Creates a mew {@link Item}.
       * 
       * @param item
       *    The item to create.
       */
      public Item(Item item) {
            this(item.getId(), item.getAmount());
      }
      
      public Item copy() {
    	  return new Item(id, amount);
      }
      
      public static boolean valid(Item item) {
          return item != null && item.id > 0 && item.amount > 0;
      }

      /**
       * Adds a specified amount to this item.
       * 
       * @param amount
       *    The amount to add.
       */
      public void add(int amount) {
            this.amount += amount;
      }
      
      /**
       *  Removes a specified amount from this item.
       *  
       *  @param amount
       *      The amount to remove.
       */
      public void remove(int amount) {
            this.amount -= amount;
      }

      @Override
      public boolean equals(Object other) {
            if (!(other instanceof Item)) {
                  return false;
            }

            final Item item = (Item) other;

            return item.getId() == id && item.getAmount() == amount;
      }
      
      /**
       * Gets the id of this item.
       * 
       * @return id
       */
      public int getId() {
            return id;
      }

      /**
       * Gets the amount of this item.
       * 
       * @return amount
       */
      public int getAmount() {
            return amount;
      }
      
      /**
       * Sets the amount of this item.
       * 
       * @param amount
       *    The amount to set.
       */
      public void setAmount(int amount) {
            this.amount = amount;
      }

      /**
       * Gets the high alchemy value.
       * 
       * @return The high alchemy value.
       */
      public int getHighAlch() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];

            if (def == null) {
                  return 0;
            }

            return def.getHighAlch();
      }

      /**
       * Gets the low alchemy value.
       * 
       * @return The low alchemy value.
       */
      public int getLowAlch() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];

            if (def == null) {
                  return 0;
            }

            return def.getLowAlch();
      }

      /**
       * Gets an item name from the ItemDefinitions.json
       */
      public String getName() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];
            return def == null ? "Unarmed" : def.getName();
      }

      /**
       * Gets the amount of gold this item is worth.
       * 
       * @return value
       */
      public int getValue() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];

            if (def == null) {
                  return 0;
            }

            return def.getShopValue();
      }

      /**
       * Gets the weight of the item.
       * 
       * @return The weight.
       */
      public double getWeight() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];

            if (def == null) {
                  return 0.0;
            }

            return 0.0;
      }

      @Override
      public int hashCode() {
            return Objects.hash(id, amount);
      }

      /**
       * Determines if the item is stackable.
       * 
       * @return The items stackability.
       */
      public boolean isStackable() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];
            return def != null && (def.isStackable() || def.isNoted());
      }

      /**
       * Gets the noted {@link Item} for this item.
       * 
       * @return The noted item.
       */
      public Item noted() {
            final ItemDefinition def = ItemDefinition.getDefinitions()[getId()];
            return def.getNotedId() != -1 ? new Item(def.getNotedId(), amount) : this;
      }
      
      public ItemDefinition definition() {
    	  return ItemDefinition.lookup(id);
      }

      @Override
      public int size() {
            return 0;
      }

      @Override
      public String toString() {
            return "[name=" + getName() + ", id=" + id + ", amount=" + amount + "]";
      }

	@Override
	public EntityType type() {
		return EntityType.ITEM;
	}

}
