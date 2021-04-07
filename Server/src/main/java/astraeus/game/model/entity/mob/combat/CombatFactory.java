package astraeus.game.model.entity.mob.combat;

import astraeus.game.model.entity.mob.combat.formula.impl.MagicFormula;
import astraeus.game.model.entity.mob.combat.formula.impl.MeleeFormula;
import astraeus.game.model.entity.mob.combat.formula.impl.RangeFormula;
import astraeus.game.model.entity.mob.combat.type.magic.Magic;
import astraeus.game.model.entity.mob.combat.type.melee.Melee;
import astraeus.game.model.entity.mob.combat.type.range.Range;
import astraeus.game.model.entity.mob.combat.weapon.WeaponDefinition;
import astraeus.game.model.entity.mob.combat.weapon.WeaponFactory;
import astraeus.game.model.entity.mob.player.Player;

public final class CombatFactory {

	private static final Melee melee = new Melee();
	private static final Range range = new Range();
	private static final Magic magic = new Magic();

	private static final RangeFormula rangeFormula = new RangeFormula();
	private static final MagicFormula magicFormula = new MagicFormula();
	private static final MeleeFormula meleeFormula = new MeleeFormula();

	private CombatFactory() {
		throw new InstantiationError("CombatFactory cannot be instantiated!");
	}

	public static Melee getMelee() {
		return melee;
	}

	public static Range getRange() {
		return range;
	}

	public static Magic getMagic() {
		return magic;
	}

	public static MeleeFormula getMeleeFormula() {
		return meleeFormula;
	}

	public static MagicFormula getMagicFormula() {
		return magicFormula;
	}

	public static RangeFormula getRangeFormula() {
		return rangeFormula;
	}

	public static int getAnimation(Player player) {
		if (player.getEquipment().hasWeapon()) {
			WeaponDefinition def = WeaponDefinition.get(player.getEquipment().getWeapon().getId());
			return def.getAnimations()[WeaponFactory.getAttackStyleConfig(player)];
		} else {
			switch (player.getCombat().getAttackType()) {
			default:
			case ACCURATE:
			case DEFENSIVE:
				return 422;
			case AGGRESSIVE:
				return 423;
			}
		}
	}

}
