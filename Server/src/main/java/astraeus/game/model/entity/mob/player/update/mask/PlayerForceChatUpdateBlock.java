package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players forced
 * text.
 * 
 * @author SeVen
 */
public class PlayerForceChatUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerForceChatUpdateBlock}.
	 */
	public PlayerForceChatUpdateBlock() {
		super(0x4, UpdateFlag.FORCED_CHAT);
	}

	@Override
	public void encode(Player entity, GamePacketBuilder builder) {
		builder.writeString(entity.getForcedChat());
	}

}
