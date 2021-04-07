package astraeus.game.model.entity.mob.combat.formula.impl;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.AttackStyle;
import astraeus.game.model.entity.mob.combat.attack.AttackType;
import astraeus.game.model.entity.mob.combat.formula.Formula;
import astraeus.game.model.entity.mob.player.collect.Equipment;
import astraeus.net.packet.out.ServerMessagePacket;
import astraeus.util.RandomUtils;

public final class MeleeFormula extends Formula {
	
	public static void main(String[] args) {
		final int effectiveStrength = 350;
		final int strengthBonus = 43;

		double damage = 1 + 1 / 3.0;
		damage += effectiveStrength / 10.0;
		damage += strengthBonus / 80.0;
		damage += effectiveStrength * strengthBonus / 640.0;
		
		System.out.println(damage);
	}

	@Override
	public int calculateMaxHit(Mob entity) {
		int effectiveStrength = Formula.getEffectiveStrength(entity);

		switch (entity.getCombat().getAttackType()) {
		case AGGRESSIVE:
			effectiveStrength += 3;
			break;
		case CONTROLLED:
			effectiveStrength += 1;
			break;
		default:
			break;
		}

		final int strengthBonus = entity.getBonuses()[Equipment.STRENGTH];

		double damage = 1 + 1 / 3.0;
		damage += effectiveStrength / 10.0;
		damage += strengthBonus / 80.0;
		damage += effectiveStrength * strengthBonus / 640.0;
		
		if (entity.isPlayer()) {
			damage = (int) (damage * entity.getPlayer().getSpecial().getMaxHitMultiplier());
	
			if (Equipment.isWearingFullVoidMelee(entity.getPlayer())) {
				damage *= 1.10;
			}
			
			if (Equipment.isWearingDharoks(entity.getPlayer())) {
				int health = entity.getMaximumHealth() - entity.getCurrentHealth();
				
				if (health <= -1) {
					health = 0;
				}
				
				damage += (health * 0.01) * damage;
			}
		}

		return (int) damage;
	}

	@Override
	public boolean isAccurate(Mob attacker, Mob defender) {
		if (attacker == null || defender == null) {
			return false;
		}
		
		int attackRoll, defenceRoll;

		AttackStyle style = attacker.getCombat().getAttackStyle();
		AttackType type = attacker.getCombat().getAttackType();
		int level = Formula.getEffectiveAttack(attacker);
		int bonus = attacker.getBonuses()[style.getOffensiveSlot()];

		attackRoll = getAttackRoll(level, bonus, type);
		
//		System.out.println(String.format("level=%s, bonus=%s, type=%s, style=%s", level, bonus, type, style));

		if (attacker.isPlayer()) {
			attackRoll = (int) (attackRoll * attacker.getPlayer().getSpecial().getAccuracyMultiplier());
			
			if (Equipment.isWearingFullVoidMelee(attacker.getPlayer())) {
				attackRoll = (int) (attackRoll * 1.10);
			}
		}


		type = defender.getCombat().getAttackType();
		level = Formula.getEffectiveDefence(defender);
		bonus = defender.getBonuses()[style.getDefensiveSlot()];

		defenceRoll = getDefenceRoll(level, bonus, type);

		if (attacker.isPlayer()) {
			attacker.getPlayer().queuePacket(new ServerMessagePacket(String.format("[MELEE] Attack roll: [%s] Defence roll: [%s]", attackRoll, defenceRoll) + " " + attacker));
		}

		return RandomUtils.random(attackRoll) > RandomUtils.random(defenceRoll);
	}

}

