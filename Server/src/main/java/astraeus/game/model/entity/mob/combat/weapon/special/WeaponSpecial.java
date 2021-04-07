package astraeus.game.model.entity.mob.combat.weapon.special;

import astraeus.game.model.Animation;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.dmg.Hit;

/**
 * Handles weapon specials
 * 
 * @author Michael | Chex
 *
 */
public interface WeaponSpecial {

	/**
	 * Builds the attack for the special.
	 * 
	 * @param attacker
	 *            The attacker.
	 * @param defender
	 *            The defender.
	 */
	public void build(Mob attacker, Mob defender);

	/**
	 * Executes when the special attack hits.
	 * 
	 * @param attacker
	 *            The attacker.
	 * @param defender
	 *            The defender.
	 * @param hit
	 *            The hit.
	 */
	public void execute(Mob attacker, Mob defender, Hit hit);

	/**
	 * Gets the multiplier boost for accuracy.
	 * 
	 * @return
	 */
	public double getAccuracyMultiplier();

	/**
	 * Handles the weapon special
	 * 
	 * @param player
	 */
	public Animation getAnimation();

	/**
	 * Gets the multiplier boost for defense.
	 * 
	 * @return
	 */
	public double getMaxHitMultiplier();

	/**
	 * The amount of weapon special needed
	 * 
	 * @return
	 */
	public int getSpecialAmountRequired();

}

