package rs2.game.model.entity.mob.combat.dmg;

import java.util.Timer;
import java.util.TimerTask;

import rs2.game.model.entity.item.ItemDefinition;
import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.packet.out.ServerMessagePacket;
import rs2.net.packet.out.SetPoisonPacket;
import rs2.util.RandomUtils;

/**
 * Represents a detriment that can damage entities for a period of time.
 * 
 * @author SeVen
 */
public class Poison {

	public static enum PoisonType {
		
		NONE(0),

		REGULAR(1),

		VENOM(2);
		
		/**
		 * The code that indicates the poisons type.
		 */
		private final int type;

		private PoisonType(int type) {
			this.type = type;
		}

		/**
		 * Gets the type of poision
		 */
		public int getType() {
			return type;
		}	
		
	}

	/**
	 * Represents the type of poison according to RuneScape.
	 */
	public static enum DamageTypes {

		/**
		 * Represents the type of poison which deals 1's and 2's.
		 */
		WEAK(new int[] { 1, 2 }),

		/**
		 * Represents the type of poison which deals 4's and 2's.
		 */
		DEFAULT(new int[] { 4, 2 }),

		/**
		 * Represents the type of poison which deals 5's and 3's.
		 */
		STRONG(new int[] { 5, 3 }),

		/**
		 * Represents the type of poison which deals 6's and 4's.
		 */
		SUPER(new int[] { 6, 4 }),

		/**
		 * Represents the type of poison that deals 8's and 6's.
		 */
		NPC(new int[] { 8, 6 });

		/**
		 * The possible hits for this type.
		 */
		private final int[] hits;

		/**
		 * Creates a new {@link DamageTypes}.
		 * 
		 * @param hits
		 *            The possible hits for this type.
		 */
		private DamageTypes(int[] hits) {
			this.hits = hits;
		}

		/**
		 * Gets a random damage.
		 * 
		 * @return The damage.
		 */
		public int getDamage() {
			for (Integer damage : hits) {
				if (RandomUtils.random(hits.length) == 1) {
					return damage;
				}
			}
			return hits[0];

		}

		/**
		 * @return the damage
		 */
		public int[] getHits() {
			return hits;
		}
	}

	/**
	 * Deals damage to an entity in the form of poison.
	 * 
	 * @param entity
	 *            The entity to deal damage to.
	 * 
	 * @param poisonType
	 *            The type of poison to deal.
	 *
	 * @param damageTypes
	 */
	public static void appendPoison(Mob attacker, Mob entity, PoisonType poisonType, DamageTypes damageTypes) {
		if (entity.isPoisoned()) {
			return;
		}
		
		boolean can = false;
		
		int random = RandomUtils.random(1, 35);
		
		if (random > 15) {
			can = true;
		}
		
		if (!can) {
			return;
		}

		if (entity.isPlayer()) {
			Player player = entity.getPlayer();

			if (player.getLastPoisoned().elapsed() < player.getImmunity()) {
				return;
			}
		}

		entity.setPoisoned(true);
		entity.setPoisonType(damageTypes);

		if (entity.isPlayer()) {
			entity.getPlayer().queuePacket(new SetPoisonPacket(poisonType));
		}

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {

			private int seconds = 0;

			private int damage = damageTypes.getDamage();

			private int antiTimer = entity.getAntipoisonTimer();

			@Override
			public void run() {
				if (entity == null || !entity.isRegistered()) {
					this.cancel();
					return;
				}

				if (entity.getAntipoisonTimer() > 0) {
					entity.setAntipoisonTimer(--antiTimer);
				}

				if (entity.isPlayer()) {
					if (!entity.getPlayer().isPoisoned()) {
						remove(attacker, entity, false);
						this.cancel();
						return;
					}
				}

				if (entity.getAntipoisonTimer() <= 0) {

					if (seconds >= 15) {

						damage--;

						if (damage <= 0) {
							remove(attacker, entity, false);
							this.cancel();
							return;
						}

						entity.hit(attacker, new Hit(damage, poisonType == PoisonType.REGULAR ? HitType.POISON : HitType.VENOM, DamageType.NONE));

						seconds = 0;
					}

				}

				if (entity.isDead() || entity.getCurrentHealth() <= 0) {
					remove(attacker, entity, true);
					this.cancel();
					return;
				}
				seconds++;
			}

		}, 0, 1000);

		if (entity.isPlayer()) {
			entity.getPlayer().queuePacket(new ServerMessagePacket("You have been poisoned."));
		}
	}

	public static void remove(Mob attacker, Mob entity, boolean death) {
		if (entity.isPlayer()) {

			Player player = entity.getPlayer();

			player.queuePacket(new SetPoisonPacket(PoisonType.NONE));
			player.setPoisoned(false);
		}
	}

	/**
	 * Gets the type of poison for a specified weapon.
	 * 
	 * @param id
	 *            The id of the weapon.
	 * 
	 * @return The strength of the poison.
	 */
	public static DamageTypes getPoisonTypeForWeapon(int id) {

		if (Poison.isWeaponPoisonous(id)) {

			String name = ItemDefinition.lookup(id).getName();

			if (name.contains("(p)")) {
				return DamageTypes.WEAK;
			} else if (name.contains("(p+)")) {
				return DamageTypes.STRONG;
			} else if (name.contains("(p++)")) {
				return DamageTypes.SUPER;
			}

		}
		return DamageTypes.DEFAULT;
	}

	/**
	 * Determines if a weapon if poisonous
	 * 
	 * @param id
	 *            The id of the weapon to check.
	 */
	public static boolean isWeaponPoisonous(int id) {
		
		if (ItemDefinition.lookup(id) == null) {
			return false;
		}
		
		ItemDefinition def = ItemDefinition.lookup(id);
		
		return def.getName().contains("(p)") || def.getName().contains("(p+)") || def.getName().contains("(p++)");
	}

}

