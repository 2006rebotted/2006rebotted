package rs2.game.model.entity.mob.combat.weapon;

import rs2.game.model.entity.mob.combat.AttackStyle;
import rs2.game.model.entity.mob.combat.attack.AttackType;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.SetWidgetConfigPacket;

public final class WeaponFactory {
	
	static int[] getArrows(String name) {
		switch (name) {
		default:
		case "shortbow":
		case "longbow":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							 };
		case "oak":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							   /* steel */ 886, 887, 5618, 5624, 
							 };
		case "willow":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							   /* steel */ 886, 887, 5618, 5624, 
							   /* mithril */ 888, 889, 5619, 5625, 
							 };
		case "maple":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							   /* steel */ 886, 887, 5618, 5624, 
							   /* mithril */ 888, 889, 5619, 5625, 
							   /* adamant */ 890, 891, 5620, 5626
							 };
		case "yew":
		case "magic":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							   /* steel */ 886, 887, 5618, 5624, 
							   /* mithril */ 888, 889, 5619, 5625, 
							   /* adamant */ 890, 891, 5620, 5626, 
							   /* rune */ 892, 893, 5621, 5627
							 };
		case "dark":
			return new int[] { /* bronze */ 882, 883, 5616, 5622, 
							   /* iron */ 884, 885, 5617, 5623, 
							   /* steel */ 886, 887, 5618, 5624, 
							   /* mithril */ 888, 889, 5619, 5625, 
							   /* adamant */ 890, 891, 5620, 5626, 
							   /* rune */ 892, 893, 5621, 5627,
							   /* dragon */ 11212, 11227, 11228, 11229
							 };
		}
	}
	
	
	static int[] getBolts(String name) {
		switch (name) {
		default:
		case "bronze":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236
							 };
		case "iron":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236,
							   /* iron */ 9140, 9287, 9294, 9301, 880, 9238,
							 };
		case "steel":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236,
							   /* iron */ 9140, 9287, 9294, 9301, 880, 9238,
							   /* steel */ 9141, 9288, 9295, 9302, 9239, 9336
							 };
		case "mithril":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236,
							   /* iron */ 9140, 9287, 9294, 9301, 880, 9238,
							   /* steel */ 9141, 9288, 9295, 9302, 9239, 9336,
							   /* mithril */ 9142, 9289, 9296, 9303, 9240, 9337
							 };
		case "adamant":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236,
							   /* iron */ 9140, 9287, 9294, 9301, 880, 9238,
							   /* steel */ 9141, 9288, 9295, 9302, 9239, 9336,
							   /* mithril */ 9142, 9289, 9296, 9303, 9240, 9337, 9338, 9241,
							   /* adamant */ 9143, 9290, 9297, 9304, 9339, 9242, 9340, 9243
							 };
		case "rune":
		case "armadyl":
			return new int[] { /* bronze */ 877, 878, 6061, 6062, 879, 9236,
							   /* iron */ 9140, 9287, 9294, 9301, 880, 9238,
							   /* steel */ 9141, 9288, 9295, 9302, 9239, 9336,
							   /* mithril */ 9142, 9289, 9296, 9303, 9240, 9337, 9338, 9241,
							   /* adamant */ 9143, 9290, 9297, 9304, 9339, 9242, 9340, 9243,
							   /* rune */ 9144, 9291, 9298, 9305, 9341, 9244, 9342, 9245
							 };
		}
	}

	public static WeaponInterface getType(int interfaceId) {
		switch (interfaceId) {
		case 776:
			// return WeaponInterface.REAP;
		case 1698:
			return WeaponInterface.BATTLEAXE;
		case 2276:
			return WeaponInterface.SWORD_OR_DAGGER;
		case 4705:
			return WeaponInterface.LONGSWORD_OR_SCIMITAR;
		case 2423:
			return WeaponInterface.TWO_HANDED;
		case 5570:
			return WeaponInterface.PICKAXE;
		case 3796:
			return WeaponInterface.MACE;
		case 7762:
			return WeaponInterface.CLAWS;
		case 4679:
			return WeaponInterface.SPEAR;
		case 6103:
			return WeaponInterface.STAFF;
		case 328:
			return WeaponInterface.MAGIC_STAFF;
		case 8460:
			return WeaponInterface.HALBERD;
		case 425:
			return WeaponInterface.WARHAMMER_OR_MAUL;
		case 12290:
			return WeaponInterface.WHIP;
		case 1764:
			return WeaponInterface.BOW;
		case 4446:
			return WeaponInterface.THROWN;
		case 24055:
			// return WeaponInterface.CHINCHOMPA;
		case 5855:
		default:
			return WeaponInterface.UNARMED;
		}
	}

	public static void updateAttackStyle(Player player) {
		int weaponId = player.getEquipment().hasWeapon() ? player.getEquipment().getWeapon().getId() : 0;
		final int interfaceId = WeaponDefinition.get(weaponId) != null ? WeaponDefinition.get(weaponId).getType().getInterfaceId() : 5855;
		
		switch (interfaceId) {
		case 776:
			reap(player);
			break;
		case 1698:
			battleaxe(player);
			break;
		case 2276:
			swordAndDagger(player);
			break;
		case 4705:
			longsword(player);
			break;
		case 2423:
			twoHandedSword(player);
			break;
		case 5570:
			pickaxe(player);
			break;
		case 3796:
			mace(player);
			break;
		case 7762:
			claws(player);
			break;
		case 4679:
			spear(player);
			break;
		case 6103:
			staff(player);
			break;
		case 328:
			magicStaff(player);
			break;
		case 8460:
			halbard(player);
			break;
		case 425:
			warhammar(player);
			break;
		case 5855:
			unarmed(player);
			break;
		case 12290:
			whip(player);
			break;
		case 1764:
		case 4446:
		case 24055:
			range(player);
			break;
		}
		player.queuePacket(new SetWidgetConfigPacket(43, getAttackStyleConfig(player)));
	}

	public static WeaponAttackStyle getAttackStyle(int id) {
		final int interfaceId = WeaponDefinition.get(id) != null ? WeaponDefinition.get(id).getType().getInterfaceId() : 5855;

		switch (interfaceId) {

		case 0:
		case 328:
		case 425:
		case 5855:
		case 6103:
			return WeaponAttackStyle.ACCURATE_AGGRESSIVE_DEFENSIVE;

		case 12290:
			return WeaponAttackStyle.ACCURATE_CONTROLLED_DEFENSIVE;

		case 776:
		case 1698:
		case 2276:
		case 4705:
		case 5570:
			return WeaponAttackStyle.ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE;

		case 2423:
		case 3796:
		case 7762:
			return WeaponAttackStyle.ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE;

		case 4679:
			return WeaponAttackStyle.CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE;

		case 8460:
			return WeaponAttackStyle.CONTROLLED_AGGRESSIVE_DEFENSIVE;

		case 1764:
		case 4446:
		case 24055:
			return WeaponAttackStyle.ACCURATE_RAPID_LONGRANGE;

		}
		return WeaponAttackStyle.ACCURATE_AGGRESSIVE_DEFENSIVE;
	}

	public static int getAttackStyleConfig(Player player) {
		int id = player.getEquipment().hasWeapon() ? player.getEquipment().getWeapon().getId() : 0;
		AttackType attackType = player.getCombat().getAttackType();
//		AttackStyle attackStyle = player.getCombat().getAttackStyle();
		
		switch (getAttackStyle(id)) {
		case ACCURATE_AGGRESSIVE_DEFENSIVE:
			switch (attackType) {
			case AGGRESSIVE: return 1;
			case DEFENSIVE:  return 2;
			default: 		 return 0;
			}
		case ACCURATE_CONTROLLED_DEFENSIVE:
			switch (attackType) {
			case CONTROLLED: return 1;
			case DEFENSIVE:  return 2;
			default: 		 return 0;
			}
		case ACCURATE_AGGRESSIVE_CONTROLLED_DEFENSIVE:
			switch (attackType) {
			case AGGRESSIVE: return 1;
			case CONTROLLED: return 2;
			case DEFENSIVE:  return 3;
			default: 		 return 0;
			}
		case ACCURATE_AGGRESSIVE_AGGRESSIVE_DEFENSIVE:
			switch (attackType) {
			case AGGRESSIVE: return 1;
			case DEFENSIVE:  return 3;
			default: 		 return 0;
			}
		case CONTROLLED_CONTROLLED_CONTROLLED_DEFENSIVE:
			switch (attackType) {
			case DEFENSIVE:  return 3;
			case CONTROLLED: return 0;
			default: 		 return 0;
			}
		case CONTROLLED_AGGRESSIVE_DEFENSIVE:
			switch (attackType) {
			case AGGRESSIVE: return 1;
			case DEFENSIVE:  return 2;
			default:		 return 0;
			}
		case ACCURATE_RAPID_LONGRANGE:
			switch (attackType) {
			case RAPID: 	return 1;
			case LONGRANGE: return 2;
			default: 		return 0;
			}
		}
		return 0;
	}

	private static void whip(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case CONTROLLED:
		case RAPID:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void reap(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		}
	}

	private static void battleaxe(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void swordAndDagger(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		}
		
//		player.send(new SendMessage(String.format("<img=9><col=369>You changed your attack type to [<col=ff0000>%s<col=369>] and attack style to [<col=ff0000>%s<col=369>].", player.getCombat().getAttackStyle(), player.getCombat().getAttackType())));
	}

	private static void longsword(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void twoHandedSword(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void pickaxe(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		}
	}

	private static void mace(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		}
	}

	private static void claws(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case CONTROLLED:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void staff(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case CONTROLLED:
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void magicStaff(Player player) {
		unarmed(player);
	}

	private static void spear(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case CONTROLLED:
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void halbard(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			break;
		}
	}

	private static void range(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackType(AttackType.ACCURATE);
			player.getCombat().setAttackStyle(AttackStyle.RANGED);
			break;
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.LONGRANGE);
			player.getCombat().setAttackStyle(AttackStyle.RANGED);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.LONGRANGE);
			player.getCombat().setAttackStyle(AttackStyle.RANGED);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.RAPID);
			player.getCombat().setAttackStyle(AttackStyle.RANGED);
			break;
		}
	}

	private static void warhammar(Player player) {
		unarmed(player);
	}

	private static void unarmed(Player player) {
		switch (player.getCombat().getAttackType()) {
		case ACCURATE:
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
		case CONTROLLED:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case DEFENSIVE:
		case LONGRANGE:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		case AGGRESSIVE:
		case RAPID:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			break;
		}
	}

	public static boolean updateAttackStyle(Player player, int button) {
		switch (button) {

		case 782:
		case 783:
		case 1704:
		case 1705:
		case 1707:
		case 2284:
		case 2429:
		case 2430:
		case 2432:
		case 4688:
		case 4711:
		case 4712:
		case 4714:
		case 7768:
		case 7769:
		case 7771:
		case 8468:
		case 12296:
		case 12297:
		case 12298:
			player.getCombat().setAttackStyle(AttackStyle.SLASH);
			return true;

		case 334:
		case 335:
		case 336:
		case 431:
		case 432:
		case 433:
		case 785:
		case 1706:
		case 3802:
		case 3803:
		case 3805:
		case 4687:
		case 4713:
		case 5578:
		case 5860:
		case 5861:
		case 5862:
		case 6135:
		case 6136:
		case 6137:
			player.getCombat().setAttackStyle(AttackStyle.CRUSH);
			return true;

		case 784:
		case 2282:
		case 2283:
		case 2285:
		case 2431:
		case 3804:
		case 4685:
		case 4686:
		case 5576:
		case 5577:
		case 5579:
		case 7770:
		case 8466:
		case 8467:
			player.getCombat().setAttackStyle(AttackStyle.STAB);
			return true;

		case 1770:
		case 1771:
		case 1772:
		case 4452:
		case 4453:
		case 4454:
		case 24059:
		case 24060:
		case 24061:
			player.getCombat().setAttackStyle(AttackStyle.RANGED);
			return true;

		default:
			return false;
		}
	}

	public static boolean updateAttackType(Player player, int button) {
		switch (button) {
		case 336:
		case 433:
		case 782:
		case 1772:
		case 1704:
		case 2429:
		case 2282:
		case 3802:
		case 4454:
		case 4711:
		case 5576:
		case 5860:
		case 6137:
		case 7768:
		case 12298:
		case 24059:
			player.getCombat().setAttackType(AttackType.ACCURATE);
			return true;

		case 335:
		case 432:
		case 784:
		case 785:
		case 1706:
		case 1707:
		case 2232:
		case 2284:
		case 2285:
		case 2432:
		case 3805:
		case 4713:
		case 4714:
		case 5578:
		case 5579:
		case 5862:
		case 6136:
		case 7771:
		case 8468:
			player.getCombat().setAttackType(AttackType.AGGRESSIVE);
			return true;

		case 334:
		case 431:
		case 783:
		case 1705:
		case 2430:
		case 2283:
		case 3803:
		case 4686:
		case 4712:
		case 5577:
		case 5861:
		case 6135:
		case 7769:
		case 8467:
		case 12296:
			player.getCombat().setAttackType(AttackType.DEFENSIVE);
			return true;

		case 2431:
		case 3804:
		case 4685:
		case 4687:
		case 4688:
		case 7770:
		case 8466:
		case 12297:
			player.getCombat().setAttackType(AttackType.CONTROLLED);
			return true;

		case 1771:
		case 4453:
		case 24060:
			player.getCombat().setAttackType(AttackType.RAPID);
			return true;

		case 1770:
		case 4452:
		case 24061:
			player.getCombat().setAttackType(AttackType.LONGRANGE);
			return true;

		default:
			return false;
		}
	}
}
