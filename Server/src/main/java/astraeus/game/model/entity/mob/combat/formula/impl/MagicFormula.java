package astraeus.game.model.entity.mob.combat.formula.impl;

import java.util.Optional;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.attack.AttackType;
import astraeus.game.model.entity.mob.combat.def.NpcCombatDefinition;
import astraeus.game.model.entity.mob.combat.def.SpellDefinition;
import astraeus.game.model.entity.mob.combat.formula.Formula;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.collect.Equipment;
import astraeus.util.RandomUtils;

/**
 * Handles the calculations of magic formulas.
 * 
 * @author Michael | Chex
 */
public final class MagicFormula extends Formula {
	
	@Override
	public int calculateMaxHit(Mob entity) {
		if (entity.isPlayer()) {
			Player player = entity.getPlayer();
			Optional<SpellDefinition> s = player.getMagic().getCastingSpell();
			
			if (s.isPresent()) {
				return s.get().getMaxHit();
			}
		} else {
			NpcCombatDefinition def = NpcCombatDefinition.get(entity.getId());
			if (def != null) {
				return def.getMagicMaxHit();
			}
		}
		
		return 0;
	}
	
	@Override
	public boolean isAccurate(Mob attacker, Mob defender) {
		if (attacker == null || defender == null) {
			return false;
		}
		
		if (attacker.isMob()) {
			return true;
		}
		
		int attackRoll, defenceRoll;

		AttackType type = attacker.getCombat().getAttackType();
		int level = Formula.getEffectiveMagic(attacker);
		int bonus = attacker.getBonuses()[Equipment.MAGIC];
		int defence = Formula.getEffectiveDefence(attacker);

		attackRoll = getAttackRoll(level, bonus, type);
		
		if (attacker.isPlayer()) {
			attackRoll = (int) (attackRoll * attacker.getPlayer().getSpecial().getAccuracyMultiplier());

			if (Equipment.isWearingFullVoidMage(attacker.getPlayer())) {
				attackRoll = (int) (attackRoll * 1.30);
			}
		}

		type = defender.getCombat().getAttackType();
		defence = Formula.getEffectiveDefence(defender);
		bonus = defender.getBonuses()[Equipment.MAGIC_DEFENSE];
		level = Formula.getEffectiveMagic(defender);

		defenceRoll = getDefenceRoll((int) (level * 0.7 + defence * 0.3), bonus, type);

		if (attacker.isPlayer()) {
			System.out.println(String.format("[MAGIC] Attack roll: [%s] Defence roll: [%s]", attackRoll, defenceRoll));
		}

		return RandomUtils.random(attackRoll) > RandomUtils.random(defenceRoll);
	}

}
