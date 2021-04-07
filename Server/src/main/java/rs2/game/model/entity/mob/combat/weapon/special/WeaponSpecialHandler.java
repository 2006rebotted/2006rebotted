package rs2.game.model.entity.mob.combat.weapon.special;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import rs2.game.model.entity.mob.combat.weapon.WeaponInterface;
import rs2.util.LoggerUtils;

public final class WeaponSpecialHandler {

	/**
	 * The single logger for this class.
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerUtils.getLogger(WeaponSpecialHandler.class);

	private static Map<Integer, WeaponSpecial> specials = new HashMap<Integer, WeaponSpecial>();	
	
	private static Set<Integer> buttons = new HashSet<Integer>();
	
	public WeaponSpecialHandler() {
		// TODO add specials
	}

	@SuppressWarnings("unused")
	private static void add(int weaponId, WeaponSpecial special) {
		specials.put(Integer.valueOf(weaponId), special);
		
		for (WeaponInterface type : WeaponInterface.values()) {
			buttons.add(type.getButton());
		}
	}
	
	public static boolean isButton(int weapon) {
		return buttons.stream().anyMatch(it -> it == weapon);
	}

	public static WeaponSpecial lookup(int id) {
		return specials.get(id);
	}

}
