package rs2.game.model.entity.mob.combat.formula.impl;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.attack.AttackType;
import rs2.game.model.entity.mob.combat.formula.Formula;
import rs2.game.model.entity.mob.combat.type.range.AmmoType;
import rs2.game.model.entity.mob.combat.weapon.WeaponDefinition;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.collect.Equipment;
import rs2.game.model.entity.mob.player.collect.Equipment.EquipmentDefinition;
import rs2.util.RandomUtils;

/**
 * Handles the calculations of range formulas.
 * 
 * @author Daniel & SeVen
 *
 */
public final class RangeFormula extends Formula {
	
	public static void main(String[] args) {
		final int effectiveStrength = 350;
		final int strengthBonus = -2;

		double damage = 1 + 1 / 3.0;
		damage += effectiveStrength / 10.0;
		damage += strengthBonus / 80.0;
		damage += effectiveStrength * strengthBonus / 640.0;
		
		System.out.println(damage);
	}

	@Override
	public int calculateMaxHit(Mob entity) {
		int effectiveStrength = Formula.getEffectiveRanged(entity);

		effectiveStrength += entity.getCombat().getAttackType().getAccuracyIncrease();

		int strengthBonus = entity.getBonuses()[Equipment.RANGED_STRENGTH];
		
		if (entity.isPlayer()) {
			Player player = entity.getPlayer();
			Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);

			if (item != null) {
				WeaponDefinition def = WeaponDefinition.get(item.getId());

				if (def != null && def.getRangedWeapon() != null) {
					if (player.getCombat().getAttackType() == AttackType.LONGRANGE) {
						strengthBonus++;
					}

					if (def.getRangedWeapon().getType() == AmmoType.THROWN) {
						Item ammo = player.getEquipment().get(Equipment.AMMO_SLOT);
						
						if (ammo != null) {
							EquipmentDefinition eq = EquipmentDefinition.get(ammo.getId());
							strengthBonus -= eq.getBonuses()[Equipment.RANGED_STRENGTH];
						}
					} else {
						EquipmentDefinition eq = EquipmentDefinition.get(item.getId());
						strengthBonus -= eq.getBonuses()[Equipment.RANGED_STRENGTH];
					}
				}
			}
		}
		
		double damage = 1 + 1 / 3.0;

		damage += effectiveStrength / 10.0;
		damage += strengthBonus / 80.0;
		damage += effectiveStrength * strengthBonus / 640.0;

		if (entity.isPlayer()) {
			damage = (int) (damage * entity.getPlayer().getSpecial().getMaxHitMultiplier());
			
			if (Equipment.isWearingFullVoidRange(entity.getPlayer())) {
				damage *= 1.20;
			}
		}

		return (int) damage;
	}

	@Override
	public boolean isAccurate(Mob attacker, Mob defender) {
		return isAccurate(attacker, defender, false);
	}
	
	public boolean isAccurate(Mob attacker, Mob defender, boolean ruby) {
		if (attacker == null || defender == null) {
			return false;
		}
		
		int attackRoll, defenceRoll;

		AttackType type = attacker.getCombat().getAttackType();
		int level = Formula.getEffectiveRanged(attacker);
		double bonus = attacker.getBonuses()[Equipment.RANGED_STRENGTH];

		if (attacker.isPlayer()) {
			Player player = attacker.getPlayer();
			Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);

			if (item != null) {
				WeaponDefinition def = WeaponDefinition.get(item.getId());

				if (def != null && def.getRangedWeapon() != null) {
					if (def.getRangedWeapon().getType() == AmmoType.THROWN) {
						Item ammo = player.getEquipment().get(Equipment.AMMO_SLOT);
						
						if (ammo != null) {
							EquipmentDefinition eq = EquipmentDefinition.get(ammo.getId());
							bonus -= eq.getBonuses()[Equipment.RANGED_STRENGTH];
						}
					}
				} else {
					EquipmentDefinition eq = EquipmentDefinition.get(item.getId());
					bonus -= eq.getBonuses()[Equipment.RANGED_STRENGTH];
				}
			}
		}

		attackRoll = getAttackRoll(level, (int) bonus, type);

		if (attacker.isPlayer()) {
			attackRoll = (int) (attackRoll * attacker.getPlayer().getSpecial().getAccuracyMultiplier());
			
			if (Equipment.isWearingFullVoidRange(attacker.getPlayer())) {
				attackRoll = (int) (attackRoll * 1.20);
			}
		}

		type = defender.getCombat().getAttackType();
		level = Formula.getEffectiveDefence(defender);
		bonus = defender.getBonuses()[Equipment.RANGED_DEFENSE];
		
		if (ruby) {
			bonus *= 0.45;
		}

		defenceRoll = getDefenceRoll(level, (int) bonus, type);

//		if (attacker.isPlayer()) {
//			System.out.println(String.format("[RANGE] Attack roll: [%s] Defence roll: [%s]", attackRoll, defenceRoll));
//		}

		return RandomUtils.random(attackRoll) > RandomUtils.random(defenceRoll);
	}

}

