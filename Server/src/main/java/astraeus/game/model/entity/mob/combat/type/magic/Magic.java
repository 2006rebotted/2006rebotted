package astraeus.game.model.entity.mob.combat.type.magic;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.CombatFactory;
import astraeus.game.model.entity.mob.combat.attack.Attack;
import astraeus.game.model.entity.mob.combat.type.CombatClass;
import astraeus.game.model.entity.mob.combat.type.CombatType;

public class Magic implements CombatClass {

	@Override
	public boolean canAttack(Mob attacker, Mob defender) {
		return validate(attacker, defender) && attacker.canAttack(defender, attacker.getCombat().getCombatType());
	}
	
	@Override
	public boolean isAccurate(Mob attacker, Mob defender) {
		return CombatFactory.getMagicFormula().isAccurate(attacker, defender);
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
		return CombatType.MAGIC;
	}
}

