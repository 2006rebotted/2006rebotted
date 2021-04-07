package astraeus.game.model.entity.mob.combat.type.range;

import java.util.HashMap;
import java.util.Map;

import astraeus.game.model.Graphic;
import astraeus.game.model.Projectile;

public enum Ammo {

	BRONZE_JAVELIN(new int[] { 825, 831, 5642, 5648 }, new Graphic(206, 1, true), new Projectile(200, true)),
	IRON_JAVELIN(new int[] { 826, 832, 5643, 5649 }, new Graphic(207, 1, true), new Projectile(201, true)),
	STEEL_JAVELIN(new int[] { 827, 833, 5644, 5650 }, new Graphic(208, 1, true), new Projectile(202, true)),
	MITHRIL_JAVELIN(new int[] { 828, 834, 5645, 5651 }, new Graphic(209, 1, true), new Projectile(203, true)),
	ADAMANT_JAVELIN(new int[] { 829, 835, 5646, 5652 }, new Graphic(210, 1, true), new Projectile(204, true)),
	RUNE_JAVELIN(new int[] { 830, 836, 5647, 5653 }, new Graphic(211, 1, true), new Projectile(205, true)),

	BRONZE_THROWNAXE(new int[] { 800 }, new Graphic(43, 1, true), new Projectile(36, true)),
	IRON_THROWNAXE(new int[] { 801 }, new Graphic(42, 1, true), new Projectile(35, true)),
	STEEL_THROWNAXE(new int[] { 802 }, new Graphic(44, 1, true), new Projectile(37, true)),
	MITHRIL_THROWNAXE(new int[] { 803 }, new Graphic(45, 1, true), new Projectile(38, true)),
	ADAMANT_THROWNAXE(new int[] { 804 }, new Graphic(46, 1, true), new Projectile(39, true)),
	RUNE_THROWNAXE(new int[] { 805 }, new Graphic(48, 1, true), new Projectile(41, true)),

	BRONZE_DART(new int[] { 806, 812, 5628, 5635 }, new Graphic(232, 1, true), new Projectile(226, true)),
	IRON_DART(new int[] { 807, 813, 5629, 5636 }, new Graphic(233, 1, true), new Projectile(227, true)),
	STEEL_DART(new int[] { 808, 814, 5630, 5637 }, new Graphic(234, 1, true), new Projectile(228, true)),
	MITHRIL_DART(new int[] { 809, 815, 5631, 5638 }, new Graphic(235, 1, true), new Projectile(229, true)),
	ADAMANT_DART(new int[] { 810, 816, 5632, 5639 }, new Graphic(236, 1, true), new Projectile(230, true)),
	RUNE_DART(new int[] { 811, 817, 5633, 5640 }, new Graphic(237, 1, true), new Projectile(231, true)),
	DRAGON_DART(new int[] { 11230, 11231 }, new Graphic(1123, 5, true), new Projectile(1122, true)),

	BRONZE_KNIFE(new int[] { 864, 870, 5654, 5651 }, new Graphic(219, 1, true), new Projectile(213, true)),
	IRON_KNIFE(new int[] { 863, 871, 5655, 5662 }, new Graphic(220, 1, true), new Projectile(214, true)),
	STEEL_KNIFE(new int[] { 865, 872, 5656, 5663 }, new Graphic(221, 1, true), new Projectile(215, true)),
	MITHRIL_KNIFE(new int[] { 866, 873, 5657, 5664 }, new Graphic(222, 1, true), new Projectile(216, true)),
	ADAMANT_KNIFE(new int[] { 867, 875, 5659, 5666 }, new Graphic(223, 1, true), new Projectile(217, true)),
	RUNE_KNIFE(new int[] { 868, 876, 5660, 5667 }, new Graphic(224, 1, true), new Projectile(218, true)),

	BRONZE_ARROW(new int[] { 882, 883, 5616, 5622 }, new Graphic(19, true), new Graphic(1104, true), new Projectile(10, true)),
	IRON_ARROW(new int[] { 884, 885, 5617, 5623 }, new Graphic(18, true), new Graphic(1105, true), new Projectile(9, true)),
	STEEL_ARROW(new int[] { 886, 887, 5618, 5624 }, new Graphic(20, true), new Graphic(1106, true), new Projectile(11, true)),
	MITHRIL_ARROW(new int[] { 888, 889, 5619, 5625 }, new Graphic(21, true), new Graphic(1107, true), new Projectile(12, true)),
	ADAMANT_ARROW(new int[] { 890, 891, 5620, 5626 }, new Graphic(22, true), new Graphic(1108, true), new Projectile(13, true)),
	RUNE_ARROW(new int[] { 892, 893, 5621, 5627 }, new Graphic(24, true), new Graphic(1109, true), new Projectile(15, true)),
	DRAGON_ARROW(new int[] { 11212, 11227, 0, 11228 }, new Graphic(11229, true), new Graphic(1111, true), new Projectile(17, true)),
	
	BOLT_RACK(new int[] { 4740 }, new Graphic(28, true), new Projectile(27, true)),

	BOLTS(new int[] { 9144, 9245, 9242, 9241, 9243, 9244 }, new Graphic(28, true), new Projectile(27, true));

	private final int[] ammo;
	private final Graphic drawback;
	private final Graphic doubleDrawback;
	private final Projectile projectile;

	private Ammo(int[] ammo, Graphic drawback, Graphic doubleDrawback, Projectile projectile) {
		this.ammo = ammo;
		this.drawback = drawback;
		this.doubleDrawback = doubleDrawback;
		this.projectile = projectile;
	}

	private Ammo(int[] ammo, Graphic drawback, Projectile projectile) {
		this.ammo = ammo;
		this.drawback = drawback;
		this.doubleDrawback = null;
		this.projectile = projectile;
	}
	
	private static Map<Integer, Ammo> ammoData = new HashMap<Integer, Ammo>();

	public static Ammo lookup(int id) {
		return ammoData.get(Integer.valueOf(id));
	}
	
	static {
		for (final Ammo ammo : values()) {
			final int[] ids = ammo.getAmmo();
			for (final int id : ids) {
				ammoData.put(id, ammo);				
			}
		}
	}

	public int[] getAmmo() {
		return ammo;
	}

	public Graphic getDoubleDrawback() {
		return doubleDrawback;
	}

	public Graphic getDrawback() {
		return drawback;
	}

	public Projectile getProjectile() {
		return projectile;
	}

}
