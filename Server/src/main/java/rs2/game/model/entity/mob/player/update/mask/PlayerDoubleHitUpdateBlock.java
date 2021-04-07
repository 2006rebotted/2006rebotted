package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players second
 * hit mark.
 * 
 * @author SeVen
 */
public class PlayerDoubleHitUpdateBlock extends PlayerUpdateBlock {

    /**
     * Creates a new {@link PlayerDoubleHitUpdateBlock}.
     */
    public PlayerDoubleHitUpdateBlock() {
	super(0x200, UpdateFlag.DOUBLE_HIT);
    }

    @Override
    public void encode(Player entity, GamePacketBuilder builder) {

    }

}
