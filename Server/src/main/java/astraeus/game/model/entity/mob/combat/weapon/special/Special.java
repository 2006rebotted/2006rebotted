package astraeus.game.model.entity.mob.combat.weapon.special;

import astraeus.game.model.Animation;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.weapon.WeaponDefinition;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.net.packet.out.SetSpecialAmountPacket;
import astraeus.net.packet.out.SetWidgetOffsetPacket;
import astraeus.net.packet.out.SetWidgetStringPacket;
import astraeus.net.packet.out.SetWidgetVisibilityPacket;

public class Special {

	private final Player player;
	private WeaponSpecial special;
	private int amount;

	public Special(Player player) {
		this.player = player;
		amount = 100;
	}

	public void update() {
		updateAmount();
		updateText();
		player.queuePacket(new SetSpecialAmountPacket());
	}

	private void build(Mob defender, int weapon) {
		special = WeaponSpecialHandler.lookup(weapon);
		updateText();
		
		if (isActive() && hasRequiredAmount()) {
			special.build(player, defender);
		}
	}

	public void execute(Mob defender, Hit hit) {
		if (isActive()) {
			special.execute(player, defender, hit);
			deduct();
		}

		reset();
	}

	public void addSpecial(int amount) {
		this.amount += amount;

		if (this.amount < 0) {
			this.amount = 0;
		}

		update();
	}

	public void deduct() {
		this.amount -= getRequiredAmount();

		if (this.amount < 0) {
			this.amount = 0;
		}

		update();
	}

	public void reset() {
		if (special != null) {
			special = null;
			update();
		}
	}

	public boolean isActive() {
		return special != null;
	}

	public WeaponSpecial getSpecial() {
		return special;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public boolean clickButton(int button) {
		if (!WeaponSpecialHandler.isButton(button)) {
			return false;
		}

		Item weapon = player.getEquipment().getWeapon();

		if (weapon != null) {
			WeaponDefinition def = WeaponDefinition.get(weapon.getId());

			if (WeaponSpecialHandler.lookup(weapon.getId()) == null) {
				return false;
			}
			
			if (def.getId() == 4153) {
				if (player.getCombat().getAttackedBy() != null) {
					build(player.getCombat().getAttackedBy(), 4153);
					player.getCombat().attack(player.getCombat().getAttackedBy());
					player.getCombat().execute();
					reset();
					return true;
				} else if (player.getCombat().getLastAttacked() != null) {
					build(player.getCombat().getLastAttacked(), 4153);
					player.getCombat().attack(player.getCombat().getLastAttacked());
					player.getCombat().execute();
					reset();
					return true;
				}
			}

			if (!isActive()) {
				build(player.getCombat().getDefender(), weapon.getId());
			} else {
				reset();
			}
			
			return true;
		}

		return false;
	}

	public double getAccuracyMultiplier() {
		if (!isActive()) {
			return 1;
		}

		return special.getAccuracyMultiplier();
	}

	public Animation getAnimation() {
		if (!isActive()) {
			return null;
		}

		return special.getAnimation();
	}

	public double getMaxHitMultiplier() {
		if (!isActive()) {
			return 1;
		}

		return special.getMaxHitMultiplier();
	}

	public int getRequiredAmount() {
		if (!isActive()) {
			return 0;
		}

		return special.getSpecialAmountRequired();
	}

	public boolean hasRequiredAmount() {
		return getRequiredAmount() <= amount;
	}

	public void updateAmount() {
		Item item = player.getEquipment().getWeapon();

		if (item != null) {
			final WeaponDefinition def = WeaponDefinition.get(item.getId());
			if (WeaponSpecialHandler.lookup(item.getId()) != null) {
				int id = def.getType().getSpecialStringId();
				int specialCheck = 100;
				for (int i = 0; i < 10; i++) {
					id--;
					player.queuePacket(new SetWidgetOffsetPacket (amount >= specialCheck ? 500 : 0, 0, id));
					specialCheck -= 10;
				}
			}
		}
	}

	public void updateInterface() {
		Item item = player.getEquipment().getWeapon();


		if (item == null || WeaponSpecialHandler.lookup(item.getId()) != null) {
			if (item != null) {
				WeaponDefinition def = WeaponDefinition.get(item.getId());
				player.queuePacket(new SetWidgetVisibilityPacket (def.getType().getLayerId(), false));
			}
		} else {
			player.queuePacket(new SetWidgetVisibilityPacket (7549, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7561, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7574, true));
			player.queuePacket(new SetWidgetVisibilityPacket (12323, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7599, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7674, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7474, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7499, true));
			player.queuePacket(new SetWidgetVisibilityPacket (8493, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7574, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7624, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7699, true));
			player.queuePacket(new SetWidgetVisibilityPacket (7800, true));
		}
	}

	public void updateText() {
		Item weapon = player.getEquipment().get(3);

		if (weapon != null) {
			WeaponDefinition def = WeaponDefinition.get(weapon.getId());


			if (WeaponSpecialHandler.lookup(weapon.getId()) != null) {
				String col = isActive() ? "<col=ffff00>" : "<col=0>";
				player.queuePacket(new SetWidgetStringPacket (String.format("%sSpecial Attack - %s%%", col, amount), def.getType().getSpecialStringId()));
				
				// TODO special enabled packet?
			}
		}
	}
}
