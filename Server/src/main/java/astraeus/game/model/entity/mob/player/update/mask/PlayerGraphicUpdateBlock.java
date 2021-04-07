package astraeus.game.model.entity.mob.player.update.mask;

import astraeus.game.model.Graphic;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.update.PlayerUpdateBlock;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.net.codec.ByteOrder;
import astraeus.net.codec.game.GamePacketBuilder;

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
