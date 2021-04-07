package rs2.game.model.entity.mob.combat.formula;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.attack.AttackType;
import rs2.game.model.entity.mob.player.Prayer.PrayerType;
import rs2.game.model.entity.mob.player.skill.Skill;

public abstract class Formula {

	public abstract int calculateMaxHit(Mob attacker);

	public abstract boolean isAccurate(Mob attacker, Mob defender);

	public static int getAttackRoll(int level, int bonus, AttackType type) {
		final int effectiveLevel = level + type.getAccuracyIncrease() + 8;
		return (int) Math.ceil(effectiveLevel * (1 + bonus / 64.0) / 4.0);
	}

	public static int getDefenceRoll(int level, int bonus, AttackType type) {
		final int effectiveLevel = level + type.getDefensiveIncrease() + 8;
		return (int) Math.ceil(effectiveLevel * (1 + bonus / 64.0) / 4.0);
	}

	public static final int getEffectiveAttack(Mob entity) {
		double level = entity.getSkills().getLevel(Skill.ATTACK);

		if (entity.isPlayer()) {
			if (entity.getPlayer().getPrayer().active(PrayerType.CLARITY_OF_THOUGHT)) {
				level *= 1.05;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.IMPROVED_REFLEXES)) {
				level *= 1.10;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.INCREDIBLE_REFLEXES)) {
				level *= 1.15;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.CHIVALRY)) {
				level *= 1.15;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.PIETY)) {
				level *= 1.20;
			}
		}

		return (int) level;
	}

	public static final int getEffectiveDefence(Mob entity) {
		double level = entity.getSkills().getLevel(Skill.DEFENCE);

		if (entity.isPlayer()) {
			if (entity.getPlayer().getPrayer().active(PrayerType.THICK_SKIN)) {
				level *= 1.05;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.ROCK_SKIN)) {
				level *= 1.10;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.STEEL_SKIN)) {
				level *= 1.15;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.CHIVALRY)) {
				level *= 1.20;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.PIETY)) {
				level *= 1.25;
			}
		}

		return (int) level;
	}

	public static final int getEffectiveMagic(Mob entity) {
		double level = entity.getSkills().getLevel(Skill.MAGIC);

		if (entity.isPlayer()) {
			if (entity.getPlayer().getPrayer().active(PrayerType.MYSTIC_WILL)) {
				level *= 1.05;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.MYSTIC_LORE)) {
				level *= 1.10;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.MYSTIC_MIGHT)) {
				level *= 1.15;
			}
		}

		return (int) level;
	}

	public static final int getEffectiveRanged(Mob entity) {
		double level = entity.getSkills().getLevel(Skill.RANGED);

		if (entity.isPlayer()) {
			if (entity.getPlayer().getPrayer().active(PrayerType.SHARP_EYE)) {
				level *= 1.05;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.HAWK_EYE)) {
				level *= 1.10;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.EAGLE_EYE)) {
				level *= 1.15;
			}
		}

		return (int) level;
	}

	public static final int getEffectiveStrength(Mob entity) {
		double level = entity.getSkills().getLevel(Skill.STRENGTH);

		if (entity.isPlayer()) {
			if (entity.getPlayer().getPrayer().active(PrayerType.BURST_OF_STRENGTH)) {
				level *= 1.05;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.SUPERHUMAN_STRENGTH)) {
				level *= 1.10;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.ULTIMATE_STRENGTH)) {
				level *= 1.15;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.CHIVALRY)) {
				level *= 1.18;
			} else if (entity.getPlayer().getPrayer().active(PrayerType.PIETY)) {
				level *= 1.23;
			}
		}

		return (int) level;
	}

}
