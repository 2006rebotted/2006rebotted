package astraeus.game.model.entity.mob.combat.type.magic;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.Movement;
import astraeus.game.model.entity.mob.combat.Combat;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.dmg.Poison;
import astraeus.game.model.entity.mob.combat.dmg.Poison.DamageTypes;
import astraeus.game.model.entity.mob.combat.dmg.Poison.PoisonType;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.Prayer.PrayerType;
import astraeus.game.model.entity.mob.player.skill.Skill;
import astraeus.net.packet.out.ServerMessagePacket;

public enum SpellEffect {
	
	TELEBLOCK((attacker, victim) -> {
		if (victim.isPlayer()) {
			return !victim.getPlayer().attr().get(Combat.TELE_BLOCK_KEY);
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();		
			if (other.getPrayer().active(PrayerType.PROTECT_FROM_MAGIC)) {
				// TODO teleblock for n amount of seconds
			} else {
				// TODO teleblock for n amount of seconds
			}			
		}
	}),

	CONFUSE((attacker, victim) -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.05;
			double delta = other.getSkills().getLevel(Skill.ATTACK) * 0.05;
			if (delta <= limit) {
				return false;
			}
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.05;
			double delta = other.getSkills().getLevel(Skill.ATTACK) * 0.05;
			other.getSkills().changeLevel(Skill.ATTACK, (int) -delta, (int) limit);
			victim.getPlayer().queuePacket(new ServerMessagePacket("You feel confused..."));
		}
	}),

	WEAKEN((attacker, victim) -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.STRENGTH) * 0.05;
			double delta = other.getSkills().getLevel(Skill.STRENGTH) * 0.05;
			if (delta <= limit) {
				return false;
			}
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.STRENGTH) * 0.05;
			double delta = other.getSkills().getLevel(Skill.STRENGTH) * 0.05;
			other.getSkills().changeLevel(Skill.STRENGTH, (int) -delta, (int) limit);
			victim.getPlayer().queuePacket(new ServerMessagePacket("You feel weakened..."));
		}
	}),

	ENFEEBLE((attacker, victim) -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.STRENGTH) * 0.10;
			double delta = other.getSkills().getLevel(Skill.STRENGTH) * 0.10;
			if (delta <= limit) {
				return false;
			}
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.STRENGTH) * 0.10;
			double delta = other.getSkills().getLevel(Skill.STRENGTH) * 0.10;
			other.getSkills().changeLevel(Skill.STRENGTH, (int) -delta, (int) limit);
			victim.getPlayer().queuePacket(new ServerMessagePacket("You have been enfeebled!"));
		}
	}),

	VULNERABILITY((attacker, victim) -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.DEFENCE) * 0.10;
			double delta = other.getSkills().getLevel(Skill.DEFENCE) * 0.10;
			if (delta <= limit) {
				return false;
			}
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.DEFENCE) * 0.10;
			double delta = other.getSkills().getLevel(Skill.DEFENCE) * 0.10;
			other.getSkills().changeLevel(Skill.DEFENCE, (int) -delta, (int) limit);
			victim.getPlayer().queuePacket(new ServerMessagePacket("You are now vulnerable!"));
		}
	}),

	STUN((attacker, victim) -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.10;
			double delta = other.getSkills().getLevel(Skill.ATTACK) * 0.10;
			if (delta <= limit) {
				return false;
			}
		}
		return true;
	} , hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.10;
			double delta = other.getSkills().getLevel(Skill.ATTACK) * 0.10;
			other.getSkills().changeLevel(Skill.ATTACK, (int) -delta, (int) limit);
			victim.getPlayer().queuePacket(new ServerMessagePacket("You have been stunned!"));
		}
	}),

	BIND((attacker, victim) -> !victim.attr().get(Movement.LOCK_MOVEMENT), hit -> attacker -> victim -> {
		victim.getMovement().lock(5);

		if (victim.isPlayer()) {
			victim.getPlayer().queuePacket(new ServerMessagePacket("You have been binded!"));
		}
	}),

	SNARE((attacker, victim) -> !victim.attr().get(Movement.LOCK_MOVEMENT), hit -> attacker -> victim -> {
		victim.getMovement().lock(10);

		if (victim.isPlayer()) {
			victim.getPlayer().queuePacket(new ServerMessagePacket("You have been snared!"));
		}
	}),

	ENTANGLE((attacker, victim) -> !victim.attr().get(Movement.LOCK_MOVEMENT), hit -> attacker -> victim -> {
		victim.getMovement().lock(15);

		if (victim.isPlayer()) {
			victim.getPlayer().queuePacket(new ServerMessagePacket("You have been entangled!"));
		}
	}),

	SMOKE_RUSH((attacker, victim) -> true, hit -> attacker -> victim -> {
		Poison.appendPoison(attacker, victim, PoisonType.REGULAR, DamageTypes.WEAK);
	}),

	SMOKE_BURST((attacker, victim) -> true, hit -> attacker -> victim -> {
		Poison.appendPoison(attacker, victim, PoisonType.REGULAR, DamageTypes.WEAK);
	}),

	SMOKE_BLITZ((attacker, victim) -> true, hit -> attacker -> victim -> {
		Poison.appendPoison(attacker, victim, PoisonType.REGULAR, DamageTypes.DEFAULT);
	}),

	SMOKE_BARRAGE((attacker, victim) -> true, hit -> attacker -> victim -> {
		Poison.appendPoison(attacker, victim, PoisonType.REGULAR, DamageTypes.DEFAULT);

			// TODO damage surrounding entities		
	}),

	SHADOW_RUSH((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.10;
			double attack = other.getSkills().getLevel(Skill.ATTACK) * 0.10;
			other.getSkills().changeLevel(Skill.ATTACK, (int) -attack, (int) limit);
		}
	}),

	SHADOW_BURST((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.10;
			double attack = other.getSkills().getLevel(Skill.ATTACK) * 0.10;
			other.getSkills().changeLevel(Skill.ATTACK, (int) -attack, (int) limit);
		}
	}),

	SHADOW_BLITZ((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.15;
			double attack = other.getSkills().getLevel(Skill.ATTACK) * 0.15;
			other.getSkills().changeLevel(Skill.ATTACK, (int) -attack, (int) limit);
		}
	}),

	SHADOW_BARRAGE((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (victim.isPlayer()) {
			Player other = victim.getPlayer();
			double limit = other.getSkills().getMaxLevel(Skill.ATTACK) * 0.15;
			double attack = other.getSkills().getLevel(Skill.ATTACK) * 0.15;

			if (attack < limit) {
				attack = limit;
			}

			other.getSkills().changeLevel(Skill.ATTACK, (int) -attack);
		}
		
		// TODO damage surrounding entities

	}),

	BLOOD_RUSH((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (attacker.isPlayer()) {
			int heal = (int) (hit.getDamage() * 0.25);
			attacker.getPlayer().getSkills().changeLevel(Skill.HITPOINTS, heal, attacker.getMaximumHealth());
		}
	}),

	BLOOD_BURST((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (attacker.isPlayer()) {
			int heal = (int) (hit.getDamage() * 0.25);
			attacker.getPlayer().getSkills().changeLevel(Skill.HITPOINTS, heal, attacker.getMaximumHealth());
		}
	}),

	BLOOD_BLITZ((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (attacker.isPlayer()) {
			int heal = (int) (hit.getDamage() * 0.25);
			attacker.getPlayer().getSkills().changeLevel(Skill.HITPOINTS, heal, attacker.getMaximumHealth());
		}
	}),

	BLOOD_BARRAGE((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (attacker.isPlayer()) {
			int heal = (int) (hit.getDamage() * 0.25);
			attacker.getPlayer().getSkills().changeLevel(Skill.HITPOINTS, heal, attacker.getMaximumHealth());
		}

		// TODO damage surrounding entities
		
	}),

	ICE_RUSH((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (!victim.attr().get(Movement.LOCK_MOVEMENT)) {
			if (victim.isPlayer()) {
				victim.getPlayer().queuePacket(new ServerMessagePacket("You have been frozen!"));
			}

			victim.getMovement().lock(5);
		}
	}),

	ICE_BURST((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (!victim.attr().get(Movement.LOCK_MOVEMENT)) {
			if (victim.isPlayer()) {
				victim.getPlayer().queuePacket(new ServerMessagePacket("You have been frozen!"));
			}

			victim.getMovement().lock(10);
		}
	}),

	ICE_BLITZ((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (!victim.attr().get(Movement.LOCK_MOVEMENT)) {
			if (victim.isPlayer()) {
				victim.getPlayer().queuePacket(new ServerMessagePacket("You have been frozen!"));
			}

			victim.getMovement().lock(15);
		}
	}),

	ICE_BARRAGE((attacker, victim) -> true, hit -> attacker -> victim -> {
		if (!victim.attr().get(Movement.LOCK_MOVEMENT)) {
			if (victim.isPlayer()) {
				victim.getPlayer().queuePacket(new ServerMessagePacket("You have been frozen!"));
			}

			victim.getMovement().lock(20);
		}

		// TODO damage surrounding entities
		
	});

	private final BiPredicate<Player, Mob> predicate;
	private final Function<Hit, Function<Player, Consumer<Mob>>> effect;

	private SpellEffect(final BiPredicate<Player, Mob> predicate, final Function<Hit, Function<Player, Consumer<Mob>>> effect) {
		this.predicate = predicate;
		this.effect = effect;
	}

	public boolean canCast(Player attacker, Mob victim) {
		return predicate.test(attacker, victim);
	}

	public void execute(Hit hit, Player attacker, Mob victim) {
		effect.apply(hit).apply(attacker).accept(victim);
	}

}
