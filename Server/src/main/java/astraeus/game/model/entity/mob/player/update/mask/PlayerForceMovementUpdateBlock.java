package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.entity.mob.player.ForceMovement;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players forced movement.
 * 
 * @author SeVen
 */
public class PlayerForceMovementUpdateBlock extends PlayerUpdateBlock {

      /**
       * Creates a new {@link PlayerForceMovementUpdateBlock}.
       */
      public PlayerForceMovementUpdateBlock() {
            super(0x400, UpdateFlag.FORCE_MOVEMENT);
      }

      @Override
      public void encode(Player entity, GamePacketBuilder builder) {

            ForceMovement movement = entity.getForceMovement();

            builder.write(movement.getStartLocation().getLocalX(), ByteModification.SUBTRACTION)
            .write(movement.getStartLocation().getLocalY(), ByteModification.SUBTRACTION)
            .write(movement.getEndLocation().getLocalX(), ByteModification.SUBTRACTION)
            .write(movement.getEndLocation().getLocalY(), ByteModification.SUBTRACTION)
            .writeShort(movement.getDurationX(), ByteModification.ADDITION, ByteOrder.LITTLE)
            .writeShort(movement.getDurationY(), ByteModification.ADDITION)
            .write(movement.getDirection().getId(), ByteModification.SUBTRACTION);
      }

}
