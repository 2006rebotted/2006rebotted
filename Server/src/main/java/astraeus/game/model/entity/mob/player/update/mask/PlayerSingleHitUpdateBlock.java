package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock implementation that displays a single hit-mark
 * on a player.
 * 
 * @author SeVen
 */
public class PlayerSingleHitUpdateBlock extends PlayerUpdateBlock {

    /**
     * Creates a new {@link PlayerSingleHitUpdateBlock}.
     */
    public PlayerSingleHitUpdateBlock() {
	super(0x20, UpdateFlag.SINGLE_HIT);
    }

    @Override
    public void encode(Player entity, GamePacketBuilder builder) {

    }

}
