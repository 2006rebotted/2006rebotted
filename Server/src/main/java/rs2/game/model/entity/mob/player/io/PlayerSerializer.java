package rs2.game.model.entity.mob.player.io;

import rs2.game.model.entity.mob.player.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A static-utility class that will serialize a {@link Player}.
 * 
 * @author Seven
 */
public final class PlayerSerializer {

      /**
       * The single instance of {@link Gson}.
       */
      public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

      private PlayerSerializer() {

      }

      /**
       * Serializes a {@code player}'s information into a file.
       * 
       * @param player The player that is being serialized.
       * 
       * @return {@code true} If this operation can be performed. {@code false} otherwise.
       */
      public static synchronized final boolean encode(Player player) {
            try {
                  new PlayerDetails(player).serialize();
                  new PlayerContainer(player).serialize(player);
                  return true;
            } catch (final Exception e) {
                  e.printStackTrace();
            }
            return false;
      }

}
