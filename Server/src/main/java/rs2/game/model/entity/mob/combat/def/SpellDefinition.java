package rs2.game.model.entity.mob.combat.def;

import java.util.HashMap;
import java.util.Map;

import rs2.game.model.Animation;
import rs2.game.model.Graphic;
import rs2.game.model.Projectile;
import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.combat.type.magic.SpellEffect;

public final class SpellDefinition {

	private static final Map<Integer, SpellDefinition> definitions = new HashMap<>();

	private final int id;
	private final String name;
	private final int level;
	private final double baseExperience;
	private final int maxHit;
	private final SpellEffect effect;
	private final Graphic start;
	private final Graphic end;
	private final Animation animation;
	private final Projectile projectile;
	private final Item weapon;
	private final Item[] runes;

	public SpellDefinition(int id, String name, int level, double baseExperience, int maxHit, SpellEffect effect, Graphic start, Graphic end, Animation animation, Projectile projectile, Item weapon, Item[] runes) {
		this.id = id;
		this.name = name;
		this.level = level;
		this.baseExperience = baseExperience;
		this.maxHit = maxHit;
		this.effect = effect;
		this.start = start;
		this.end = end;
		this.animation = animation;
		this.projectile = projectile;
		this.weapon = weapon;
		this.runes = runes;
	}
	
	public static SpellDefinition get(int id) {
		return definitions.get(id);
	}

	public static Map<Integer, SpellDefinition> getSpellDefinition() {
		return definitions;
	}

	public Animation getAnimation() {
		return animation;
	}

	public double getBaseExperience() {
		return baseExperience;
	}

	public SpellEffect getEffect() {
		return effect;
	}

	public Graphic getEnd() {
		return end;
	}

	public int getId() {
		return id;
	}

	public int getLevel() {
		return level;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public String getName() {
		return name;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public Item[] getRunes() {
		return runes;
	}

	public Graphic getStart() {
		return start;
	}

	public Item getWeapon() {
		return weapon;
	}

}
