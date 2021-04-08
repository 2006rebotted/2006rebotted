package rs2.game.model.entity.mob.player.io;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.player.Player;

import java.io.*;

/**
 * The class used to create an object and convert to json object.
 *
 * @author Seven
 */
public final class PlayerContainer {

    private Item[] inventory;

    private Item[] equipment;

    private Item[] bank;

    /**
     * Creates a new {@link PlayerContainer}.
     *
     * @param player The player who owns this container.
     *
     *        Any attributes that are placed in here will be serialized.
     */
    public PlayerContainer(Player player) {
        inventory = player.getInventory().container();
        equipment = player.getEquipment().container();
        bank = player.getBank().container();
    }

    public Item[] getInventory() {
        return inventory;
    }

    public void setInventory(Item[] inventory) {
        this.inventory = inventory;
    }

    public Item[] getEquipment() {
        return equipment;
    }

    public void setEquipment(Item[] equipment) {
        this.equipment = equipment;
    }

    public Item[] getBank() {
        return bank;
    }

    public void setBank(Item[] bank) {
        this.bank = bank;
    }

    /**
     * Serializes a {@code player}.
     */
    public void serialize(Player player) throws IOException {
    	
    	final File dir = new File("./data/characters/containers/");
    	
    	if (!dir.exists()) {
    		dir.mkdirs();
    	}
    	
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(dir.toString() + File.separator + player.getUsername() + ".json", false))) {
            writer.write(PlayerSerializer.GSON.toJson(this));
        }
    }

    /**
     * Determins if a {@code player}, can be deserialized.
     *
     * @param player The player to check.
     * @return {@code true} If a player can be deserialized, {@code false} otherwise.
     */
    public static boolean deserialize(Player player) throws Exception {
        final File file = new File(
                "./data/characters/containers/" + player.getUsername() + ".json");
        if (!file.exists()) {
            return false;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final PlayerContainer details =
                    PlayerSerializer.GSON.fromJson(reader, PlayerContainer.class);

            if (details.getInventory() != null) {
                player.getInventory().setItems(details.getInventory());
            }

            if (details.getEquipment() != null) {
                player.getEquipment().setItems(details.getEquipment());
            }

            if (details.getBank() != null) {
                player.getBank().setItems(details.getBank());
            }
        }
        return true;
    }

}
