package rs2.game.model.entity.mob.combat.type.range;

import rs2.game.model.Graphic;
import rs2.game.model.Projectile;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.combat.weapon.WeaponDefinition;
import rs2.game.model.entity.mob.player.Player;

public class Range {	

	public static Graphic getDrawbackGraphic(Player player) {
		final Item weapon = player.getEquipment().get(3);
		final Item ammo = player.getEquipment().get(13);

		if ((weapon == null) || (WeaponDefinition.get(weapon.getId()).getRangedWeapon() == null)) {
			return null;
		}

		final RangedWeaponDefinition def = WeaponDefinition.get(weapon.getId()).getRangedWeapon();

		switch (def.getType()) {
		
		case SHOT:
		case DOUBLE_SHOT:
			switch (weapon.getId()) {
			case 4214:
				return new Graphic(250, true);
			}

			if (ammo == null) {
				return null;
			}

			Ammo data = Ammo.lookup(ammo.getId());

			if (data == null) {
				return null;
			}

			if (def.getType() == AmmoType.DOUBLE_SHOT) {
				return data.getDoubleDrawback();
			}
			return data.getDrawback();

		case THROWN:
			data = Ammo.lookup(weapon.getId());

			if (data != null) {
				return data.getDrawback();
			}
			return null;
			
		}
		return null;
	}

	public static Projectile getProjectile(Player player) {
		final Item weapon = player.getEquipment().get(3);
		final Item ammo = player.getEquipment().get(13);

		if ((weapon == null) || (WeaponDefinition.get(weapon.getId()).getRangedWeapon() == null)) {
			return null;
		}

		if (weapon != null) {
			Ammo data = Ammo.lookup(weapon.getId());

			if (data != null) {
				return new Projectile(data.getProjectile());
			} else {
				switch (weapon.getId()) {

				case 10034:
					return new Projectile(909, true);

				case 10033:
					return new Projectile(908, true);

				case 6522:
					return new Projectile(442, true);
				}
			}

			if (ammo != null) {
				data = Ammo.lookup(ammo.getId());

				if (data != null) {
					return new Projectile(data.getProjectile());
				}
			}
		}

		return null;
	}

	public static Projectile getProjectile(Item weapon) {
		if ((weapon == null) || (WeaponDefinition.get(weapon.getId()).getRangedWeapon() == null)) {
			return null;
		}

		if (weapon != null) {
			Ammo data = Ammo.lookup(weapon.getId());

			if (data != null) {
				return new Projectile(data.getProjectile());
			} else {
				switch (weapon.getId()) {

				case 10034:
					return new Projectile(909, true);

				case 10033:
					return new Projectile(908, true);

				case 6522:
					return new Projectile(442, true);
				}
			}
		}

		return null;
	}

}
