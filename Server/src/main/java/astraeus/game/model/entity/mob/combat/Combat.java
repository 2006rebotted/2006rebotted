package astraeus.game.model.entity.mob.combat;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.type.CombatType;
import astraeus.game.model.entity.mob.player.attr.AttributeKey;

public final class Combat extends CombatFields {
	
	public static final AttributeKey<Boolean> TELE_BLOCK_KEY = AttributeKey.valueOf("teleblock", false);
	
	public Combat(Mob mob) {
		super(mob);
	}
	
	public void execute() {

	}
	
	public void attack(Mob victim) {
		attack(victim, false);
	}

	public void attack(Mob victim, boolean ignoreDistance) {

	}
	
	public boolean withinDistanceForAttack(Mob defender, CombatType type, boolean noMovement) {
		if (getCombatClass() == null) {
			return false;
		}

		if (type == null) {
			type = getCombatType();
		}

		@SuppressWarnings("unused")
		int dist = type == CombatType.MELEE ? 1 : type != CombatType.RANGE ? 10 : 8;

		if (!noMovement && !getEntity().isMob() && !defender.isMob() && getEntity().getMovement().isMoving() && defender.getMovement().isMoving()) {
			dist += 3;
		}
		return this.getAttackedBy().getPosition().isWithinInteractionDistance(defender.getPosition());
//		return defender.withinCombatDistance(type, getEntity(), dist, false);
	}
	
	public void reset() {
		setInCombat(false);
		setDefender(null);
		setFirstHit(true);
		setInCombat(false);
		getEntity().resetEntityInteraction();
		getEntity().setFollowing(false);
	}

}
