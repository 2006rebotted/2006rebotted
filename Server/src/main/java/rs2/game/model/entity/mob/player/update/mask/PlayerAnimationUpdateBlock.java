package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.Animation;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteModification;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players animation
 * state.
 * 
 * @author SeVen
 */
public class PlayerAnimationUpdateBlock extends PlayerUpdateBlock {

    /**
     * Creates a new {@link PlayerAnimationUpdateBlock}.
     */
    public PlayerAnimationUpdateBlock() {
	super(0x8, UpdateFlag.ANIMATION);
    }

    @Override
    public void encode(Player entity, GamePacketBuilder builder) {
	final Animation animation = entity.getAnimation();
	builder.writeShort(animation.getId(), ByteOrder.LITTLE)
	.write(animation.getDelay(), ByteModification.NEGATION);
    }

}
