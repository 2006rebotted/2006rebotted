package astraeus.game.model.entity.mob.combat.type.melee;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.CombatFactory;
import astraeus.game.model.entity.mob.combat.attack.Attack;
import astraeus.game.model.entity.mob.combat.type.CombatClass;
import astraeus.game.model.entity.mob.combat.type.CombatType;

/**
 * An implementation of the Melee combat class.
 * 
 * @author Michael | Chex
 */
public final class Melee implements CombatClass {

	@Override
	public boolean canAttack(Mob attacker, Mob defender) {
		return validate(attacker, defender) && attacker.canAttack(defender, attacker.getCombat().getCombatType());
	}
	
	@Override
	public boolean isAccurate(Mob attacker, Mob defender) {
		return CombatFactory.getMeleeFormula().isAccurate(attacker, defender);
	}

	@Override
	public Attack getAttack(Mob attacker) {
		return attacker.getCombat().getAttack();
	}

	@Override
	public void buildAttack(Mob attacker) {
		attacker.buildAttack(attacker.getCombat().getCombatType());
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

}
