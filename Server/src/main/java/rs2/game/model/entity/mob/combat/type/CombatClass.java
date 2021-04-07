package rs2.game.model.entity.mob.combat.type;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.attack.Attack;
import rs2.game.model.entity.mob.combat.dmg.Hit;

/**
 * The {@link CombatClass} holds methods for all three main combat types:
 * {@link Melee}, {@link Magic}, and {@link Range}.
 * 
 * @author Michael | Chex
 */
public interface CombatClass {

	/**
	 * Checks if the attacking entity can attack the defending entity.
	 * 
	 * @param attacker
	 *            The attacking entity.
	 * @param defender
	 *            The defending entity.
	 * @return {@code true} if the attacking entity can attack the defending
	 *         entity, {@code false} otherwise.
	 */
	public boolean canAttack(Mob attacker, Mob defender);

	/**
	 * Gets the attack information for the next hit.
	 * 
	 * @return The attack.
	 */
	public Attack getAttack(Mob attacker);

	/**
	 * Sets the attack information for the next hit.
	 */
	public void buildAttack(Mob attacker);

	/**
	 * Gets the combat type for this combat style.
	 * 
	 * @return The {@link CombatType}.
	 */
	public CombatType getCombatType();

	public boolean isAccurate(Mob attacker, Mob defender);

	/**
	 * Validates both he attacking and defending entities.
	 * 
	 * @param attacker
	 *            The attacking entity.
	 * @param defender
	 *            The defending entity.
	 * @return {@code true} if the attack and defender are valid entities,
	 *         {@code false} otherwise.
	 */
	public default boolean validate(Mob attacker, Mob defender) {
		if (attacker == null || defender == null) {
			return false;
		}

		if (attacker.isDead() || defender.isDead()) {
			return false;
		}

		if (!attacker.isRegistered() || !defender.isRegistered()) {
			return false;
		}

		return true;
	}

	/**
	 * Gets the maximum distance for this combat style.
	 * 
	 * @return The maximum distance for this attack style.
	 */
	public default boolean withinDistance(Mob attacker, Mob defender) {
		return attacker.getCombat().withinDistanceForAttack(defender, getCombatType(), false);
	}

	/**
	 * Applies a hit to the defending entity.
	 * 
	 * @param attacker
	 *            The entity attacking.
	 * @param defender
	 *            The entity defending.
	 * @param hit
	 *            The hit to apply to the defending entity.
	 * @param attack
	 *            The attack.
	 */
	public default void hit(Mob attacker, Mob defender, Hit hit, Attack attack) {

	}

}
