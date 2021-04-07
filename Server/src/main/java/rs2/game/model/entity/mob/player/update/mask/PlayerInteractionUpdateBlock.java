package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock implementation that updates a players
 * interaction.
 * 
 * @author SeVen
 */
public class PlayerInteractionUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerInteractionUpdateBlock}.
	 */
	public PlayerInteractionUpdateBlock() {
		super(0x1, UpdateFlag.ENTITY_INTERACTION);
	}

	@Override
	public void encode(Player target, GamePacketBuilder builder) {
		final Mob entity = (Mob) target.getInteractingEntity();

		if (entity != null) {
			int index = entity.getSlot();

			if (entity.isPlayer()) {
				index += Short.MAX_VALUE + 1;
			}

			builder.writeShort(index, ByteOrder.LITTLE);
		} else {
			builder.writeShort(-1, ByteOrder.LITTLE);
		}

	}

}
