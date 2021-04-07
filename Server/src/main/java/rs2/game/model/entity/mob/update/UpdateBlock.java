package rs2.game.model.entity.mob.update;

import rs2.game.model.entity.mob.Mob;
import rs2.net.codec.game.GamePacketBuilder;

/**
 * Represents an update block of the entity updating procedure.
 * 
 * @author SeVen
 */
public abstract class UpdateBlock<E extends Mob> {

	/**
	 * The mask id for this update.
	 */
	private final int mask;

	/**
	 * The enumerated update type.
	 */
	private final UpdateFlag flag;

	/**
	 * Creates a new {@link UpdateBlock}.
	 * 
	 * @param flag
	 *            The enumerated update type.
	 */
	public UpdateBlock(int mask, UpdateFlag flag) {
		this.mask = mask;
		this.flag = flag;
	}

	/**
	 * Writes the update to a buffer to be later appended to the main update
	 * block.
	 * 
	 * @param entity
	 *            The entity to write.
	 * 
	 * @param builder
	 *            The buffer that will store the data.
	 */
	public abstract void encode(E entity, GamePacketBuilder builder);

	/**
	 * Gets the enumerated update type.
	 * 
	 * @return flag
	 */
	public UpdateFlag getFlag() {
		return flag;
	}

	/**
	 * Gets the mask.
	 * 
	 * @return mask
	 */
	public int getMask() {
		return mask;
	}

}
