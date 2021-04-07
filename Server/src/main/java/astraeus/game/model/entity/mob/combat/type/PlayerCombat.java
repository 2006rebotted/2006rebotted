package astraeus.game.model.entity.mob.combat.type;

import astraeus.game.model.Graphic;
import astraeus.game.model.Priority;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.attack.AttackBuilder;
import astraeus.game.model.entity.mob.combat.attack.AttackType;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.dmg.Poison;
import astraeus.game.model.entity.mob.combat.dmg.Poison.DamageTypes;
import astraeus.game.model.entity.mob.combat.dmg.Poison.PoisonType;
import astraeus.game.model.entity.mob.combat.type.range.AmmoType;
import astraeus.game.model.entity.mob.combat.weapon.WeaponDefinition;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.Prayer.PrayerType;
import astraeus.game.model.entity.mob.player.collect.Equipment;
import astraeus.game.model.entity.mob.player.skill.Skill;
import astraeus.game.model.location.Area;
import astraeus.net.packet.out.ServerMessagePacket;

public class PlayerCombat implements EntityCombat {

	private Player player;
	private int recoil;

	public PlayerCombat(Player player) {
		this.player = player;
	}

	@Override
	public boolean canAttack(Mob attacker, Mob defender, CombatType type) {
		if (player.isDead() || defender.isDead()) {
			return false;
		}

		if (player.getCurrentHealth() == 0 || defender.getCurrentHealth() == 0) {
			return false;
		}

		if (player.getSpecial().isActive() && !player.getSpecial().hasRequiredAmount()) {
			player.queuePacket(new ServerMessagePacket("You do not have enough power to do that."));
			player.getSpecial().reset();
			return false;
		}

		if (defender.isPlayer()) {
			int difference = Math.abs(player.getCombatLevel() - defender.getPlayer().getCombatLevel());

			if (difference > 10) {
				player.queuePacket(new ServerMessagePacket("Your combat level difference is too great!"));
				return false;
			}

		}

		if (!Area.inMultiCombat(player) || !Area.inMultiCombat(defender)) {
			if (player.getCombat().getDefender() != null && !defender.equals(player.getCombat().getDefender()) && player.getCombat().getDefender().getCombat().getDefender() != null) {
				player.queuePacket(new ServerMessagePacket("You are already under in combat."));
				return false;
			}

			if (defender.getCombat().getDefender() != null && !defender.getCombat().getDefender().equals(player) && defender.getCombat().getDefender().getCombat().getDefender() != null) {
				if (defender.getCombat().getDefender().getCombat().getDefender().getId() != 6582) {
					player.queuePacket(new ServerMessagePacket("This " + (defender.isMob() ? "monster" : "player") + " is already in combat."));
					return false;
				}
			}

			if (player.getCombat().getAttackedBy() != null && !defender.equals(player.getCombat().getAttackedBy())) {
				player.queuePacket(new ServerMessagePacket("You are already under attack."));
				return false;
			}

			if (defender.getCombat().getAttackedBy() != null && !player.equals(defender.getCombat().getAttackedBy())) {
				if (defender.getCombat().getAttackedBy().getId() != 6582) {
					player.queuePacket(new ServerMessagePacket("This " + (defender.isMob() ? "monster" : "player") + " is already under attack."));
					return false;
				}
			}
		}

		if (!Area.inMultiCombat(defender)) {
			boolean underAttack = false;

			if (defender.getCombat().getAttackedBy() != null) {
				underAttack = !player.equals(defender.getCombat().getAttackedBy());
			}

			if (underAttack && defender.getCombat().getAttackedBy().getId() != 6582) {
				player.queuePacket(new ServerMessagePacket(String.format("This %s is already under attack!", defender.isMob() ? "npc" : "player")));
				return false;
			}
		}

		switch (type) {
		case MAGIC:
			return canMagicAttack(defender);
		case RANGE:
			return canRangeAttack(defender);
		default:
			return true;
		}
	}

	@Override
	public void onAttack(Mob attacker, Mob defender, Hit hit, CombatType type) {
		
		// TODO on attack
		
//		if (!player.getMagic().getSpellCasting().hasSingleSpell() && player.getSpecial().isActive()) {
//			player.getSpecial().execute(defender, hit);
//		}

		if (type == CombatType.RANGE) {
			Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);

			if (item != null) {
				int slot = Equipment.WEAPON_SLOT;
				WeaponDefinition def = WeaponDefinition.get(item.getId());

				if (def != null && def.getRangedWeapon() != null) {
					if (def.getRangedWeapon().getType() == AmmoType.THROWN) {
						slot = Equipment.WEAPON_SLOT;
					} else {
						slot = Equipment.AMMO_SLOT;
					}

					final Item ammo = player.getEquipment().get(slot);

					if (ammo != null && Poison.getPoisonTypeForWeapon(ammo.getId()) != DamageTypes.DEFAULT) {
						if (hit.getDamage() > 0) {
							Poison.appendPoison(attacker, defender, PoisonType.REGULAR, Poison.getPoisonTypeForWeapon(ammo.getId()));
						}
					}
				}
			}

			onRangeAttack(defender, hit);
		} else if (type == CombatType.MAGIC) {
			onMagicAttack(defender, hit);
		} else {
			if (type == CombatType.RANGE) {
			} else {
				if (player.getEquipment().hasWeapon()) {
					int weaponId = player.getEquipment().getWeapon().getId();

					if (Poison.isWeaponPoisonous(weaponId)) {
						Poison.appendPoison(attacker, defender, PoisonType.REGULAR, Poison.getPoisonTypeForWeapon(weaponId));
					}
				}
			}
		}

		if (player.getPrayer().active(PrayerType.SMITE) && defender.isPlayer() && hit != null) {
			defender.getPlayer().getPrayer().drain((int) (hit.getDamage() * 0.25));
		}

		if (Equipment.isWearingGuthans(player) && hit != null) {
			if (Math.random() > 0.75) {
				player.getSkills().changeLevel(Skill.HITPOINTS, hit.getDamage(), player.getMaximumHealth());
				defender.startGraphic(new Graphic(Priority.HIGH, 398));
			}
		}
		
		if (!player.getWidgets().isEmpty()) {
			player.getWidgets().close();
		}
		
	}

	@Override
	public void onHit(Mob attacker, Mob defender, Hit hit, CombatType type) {
		switch (type) {
		case MAGIC:
			if (player.getPrayer().active(PrayerType.PROTECT_FROM_MAGIC)) {
				if (hit != null) {
					if (attacker.isMob() && attacker.getId() == 6582) {
						hit.setDamage(hit.getDamage());
					} else {
						hit.setDamage(attacker.isMob() ? 0 : (int) (hit.getDamage() * 0.6));						
					}
				}
			}
			break;

		case MELEE:
			if (player.getPrayer().active(PrayerType.PROTECT_FROM_MELEE)) {
				if (attacker.isPlayer() && Equipment.isWearingVeracs(attacker.getPlayer())) {
					break;
				}

				if (hit != null) {
					if (attacker.isMob() && attacker.getId() == 6582) {
						hit.setDamage(hit.getDamage());
					} else {
						hit.setDamage(attacker.isMob() ? 0 : (int) (hit.getDamage() * 0.6));						
					}
				}
			}
			break;

		case RANGE:
			if (player.getPrayer().active(PrayerType.PROTECT_FROM_RANGE)) {
				if (hit != null) {
					hit.setDamage(attacker.isMob() ? 0 : (int) (hit.getDamage() * 0.6));
				}
			}
			break;
		}
		
		if (player.getWidgets().isEmpty()) {
			player.getWidgets().close();
		}
		
		
	}

	@Override
	public int getCombatDelay(Mob attacker) {
		Item item = player.getEquipment().get(Equipment.WEAPON_SLOT);

		if (item != null) {
			WeaponDefinition def = WeaponDefinition.get(item.getId());

			if (def != null) {
				if (player.getCombat().getCombatType() == CombatType.RANGE) {
					int speed = def.getAttackSpeed();

					if (player.getCombat().getAttackType() == AttackType.RAPID) {
						speed--;
					}

					if (item.getId() == 12926) {
						if (player.getCombat().getDefender() != null && !player.getCombat().getDefender().isMob()) {
							speed++;
						}
					}

					return speed;
				}
				return def.getAttackSpeed();
			}
		}

		return 4;
	}

	@Override
	public void setCombatAnimations(Mob attacker) {

	}

	@Override
	public void setAttack(Mob attacker) {

	}

	@Override
	public void buildAttack(Mob attacker, CombatType type) {

	}

	private boolean canRangeAttack(Mob defender) {
		return true;
	}

	private boolean canMagicAttack(Mob defender) {
		return true;
	}

	@SuppressWarnings("unused")
	private void buildRange(AttackBuilder builder) {

	}

	@SuppressWarnings("unused")
	private void buildMagic(AttackBuilder builder) {

	}

	private void onRangeAttack(Mob defender, Hit hit) {

	}

	private void onMagicAttack(Mob defender, Hit hit) {

	}
	
	@Override
	public boolean canTakeDamage(Mob defender) {
		return !player.getMagic().isTeleporting();
	}

	public int getRecoil() {
		return recoil;
	}

	public void setRecoil(int recoil) {
		this.recoil = recoil;
	}

	@Override
	public void onDamage(Mob attacker, Mob defender, Hit hit) {
	}

}
