package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.Graphic;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players graphics.
 * 
 * @author SeVen
 */
public class PlayerGraphicUpdateBlock extends PlayerUpdateBlock {

    /**
     * Creates a new {@link PlayerGraphicUpdateBlock}.
     */
    public PlayerGraphicUpdateBlock() {
	super(0x100, UpdateFlag.GRAPHICS);
    }

    @Override
    public void encode(Player entity, GamePacketBuilder builder) {
	final Graphic graphic = entity.getGraphic();
	builder.writeShort(graphic.getId(), ByteOrder.LITTLE);
	builder.writeInt(graphic.getDelay() | graphic.getHeight());
    }

}
