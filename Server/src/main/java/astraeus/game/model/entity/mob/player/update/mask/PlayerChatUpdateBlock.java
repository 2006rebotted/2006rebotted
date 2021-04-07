package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.entity.mob.player.ChatMessage;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteModification;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players chat text.
 * 
 * @author SeVen
 */
public class PlayerChatUpdateBlock extends PlayerUpdateBlock {

      /**
       * Creates a new {@link PlayerChatUpdateBlock}.
       */
      public PlayerChatUpdateBlock() {
            super(0x80, UpdateFlag.CHAT);
      }

      @Override
      public void encode(Player entity, GamePacketBuilder builder) {
            final ChatMessage msg = entity.getChatMessage();
            final byte[] bytes = msg.getText();

            builder.writeShort(((msg.getColor() & 0xFF) << 8) + (msg.getEffect() & 0xFF), ByteOrder.LITTLE)
            .write(entity.getRights().getProtocolValue())
            .write(bytes.length, ByteModification.NEGATION).writeBytesReverse(bytes);
      }

}
