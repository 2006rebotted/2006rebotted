package rs2.game.model.entity.mob.combat.type.range;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import rs2.game.model.Animation;
import rs2.game.model.Graphic;
import rs2.game.model.Priority;
import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.CombatFactory;
import rs2.game.model.entity.mob.combat.attack.Attack;
import rs2.game.model.entity.mob.combat.dmg.DamageType;
import rs2.game.model.entity.mob.combat.dmg.Hit;
import rs2.game.model.entity.mob.combat.dmg.HitType;
import rs2.game.model.entity.mob.combat.dmg.Poison;
import rs2.game.model.entity.mob.combat.dmg.Poison.DamageTypes;
import rs2.game.model.entity.mob.combat.dmg.Poison.PoisonType;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.skill.Skill;
import rs2.net.packet.out.ServerMessagePacket;
import rs2.util.RandomUtils;

/**
 * Each enum entry holds what the effect of each bolt has on the victim of an
 * attack.
 * 
 * @author Michael | Chex
 */
public enum BoltEffect {
	
	OPAL_BOLT_E(9236, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && victim.isPlayer()) {
			hit.setDamage((int) (hit.getDamage() * 1.25));
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 749));
		}
	}),
	
	JADE_BOLT_E(9237, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && victim.isPlayer()) {
			hit.setDamage((int) (hit.getDamage() * 1.30));
			victim.getMovement().lock(3);
			victim.startAnimation(new Animation(Priority.EXTRA_HIGH, 5476));
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 755));
		}
	}),
	
	PEARL_BOLT_E(9238, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && victim.isPlayer()) {
			hit.setDamage((int) (hit.getDamage() * 1.30));
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 750));
		}
	}),
	
	TOPAZ_BOLT_E(9239, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && victim.isPlayer()) {
			victim.getSkills().changeLevel(Skill.MAGIC, -1);
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 757));
		}
	}),
	
	SAPPHIRE_BOLT_E(9240, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && victim.isPlayer()) {
			int delta = (int) (victim.getPlayer().getSkills().getLevel(Skill.PRAYER) * 0.25);
			victim.getSkills().changeLevel(Skill.PRAYER, -delta);
			attacker.getSkills().changeLevel(Skill.PRAYER, delta);
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 751));
		}
	}),
	
	EMERALD_BOLT_E(9241, hit -> attacker -> victim -> {
		Poison.appendPoison(attacker, victim, PoisonType.REGULAR, DamageTypes.STRONG);
		victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 752));
	}),
	
	RUBY_BOLT_E(9242, hit -> attacker -> victim -> {
		if (attacker.isPlayer() && attacker.getCurrentHealth() * 0.10 >= 1) {
			victim.hit(attacker, new Hit((int) (victim.getCurrentHealth() * 0.20)));
			victim.onDamage(attacker, new Hit((int) (victim.getCurrentHealth() * 0.20)));
			attacker.hit(attacker, new Hit((int) (attacker.getCurrentHealth() * 0.10)));
			victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 754));
			attacker.getPlayer().queuePacket(new ServerMessagePacket("You drain 10% of your hitpoints and 20% of your opponent's hitpoints."));
		}
	}),
	
	DIAMOND_BOLT_E(9243, hit -> attacker -> victim -> {
		
		// TODO diamond bolts
		
//		if (hit.getDamage() == 0) {
//			hit.setAs(constructDiamondHit(attacker.getPlayer()));
//		}
		hit.setDamage((int) (hit.getDamage() * 1.15));
		victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 758));
	}),

	DRAGON_BOLT_E(9244, hit -> attacker -> victim -> {

		// TODO dragon bolts
		
//		if (victim.isPlayer()) {
//			if (victim.getPlayer().getAntifireTimer() > 0 || Equipment.isWearingAntiFire(victim.getPlayer())) {
//				return;
//			}
//		}
		
		hit.setDamage((int) (hit.getDamage() * 1.45));
		victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 756));
	}),
	
	ONYX_BOLT_E(9245, hit -> attacker -> victim -> {
		hit.setDamage((int) (hit.getDamage() * 1.20));
		attacker.hit(attacker, new Hit((int) (hit.getDamage() * 0.25)));
		victim.startGraphic(new Graphic(Priority.EXTRA_HIGH, 753));
	});

	private static final Map<Integer, BoltEffect> EFFECTS = new HashMap<>();

	public static void declare() {
		for (final BoltEffect effect : values()) {
			EFFECTS.put(effect.item, effect);
		}
	}

	public static BoltEffect get(int item) {
		return EFFECTS.get(item);
	}

	private int item;

	private Function<Hit, Function<Player, Consumer<Mob>>> effect;

	private BoltEffect(int item, Function<Hit, Function<Player, Consumer<Mob>>> effect) {
		this.item = item;
		this.effect = effect;
	}

	public void execute(Hit hit, Player attacker, Mob victim) {
		final int chance = RandomUtils.random(230);

		if (chance <= 30) {
			effect.apply(hit).apply(attacker).accept(victim);
		}
	}
	
	@SuppressWarnings("unused")
	private static Hit constructDiamondHit(Player player) {
		Attack attack = player.getCombat().getAttack().copy();

		Hit hit = new Hit(0, DamageType.of(attack.getCombatType()));
		int damage = RandomUtils.random(attack.getMaxHit());

		if (CombatFactory.getRangeFormula().isAccurate(player, player.getCombat().getDefender(), true)) {
			hit = new Hit(damage, damage == attack.getMaxHit() ? HitType.CRITICAL : HitType.NORMAL, DamageType.of(attack.getCombatType()));
		}
		
		int total = 0;
		
		if (hit != null) {
			total += hit.getDamage();
		}
		
		player.getSkills().addCombatExperience(player.getCombat().getCombatType(), total);
		
		return hit;
	}

}
