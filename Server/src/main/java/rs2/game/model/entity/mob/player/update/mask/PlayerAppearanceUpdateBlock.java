package rs2.game.model.entity.mob.player.update.mask;

import rs2.game.model.entity.mob.player.Appearance;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.collect.Equipment;
import rs2.game.model.entity.mob.player.update.PlayerUpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.ByteModification;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.util.StringUtils;

/**
 * The {@link PlayerUpdateBlock} implementation that updates a players
 * appearance.
 * 
 * @author Seven
 */
public class PlayerAppearanceUpdateBlock extends PlayerUpdateBlock {

	/**
	 * Creates a new {@link PlayerAppearanceUpdateBlock}.
	 */
	public PlayerAppearanceUpdateBlock() {
		super(0x10, UpdateFlag.APPEARANCE);
	}

	@Override
	public void encode(Player player, GamePacketBuilder builder) {

		/**
		 * The container for the players properties.
		 */
		final GamePacketBuilder properties = new GamePacketBuilder();

		//gender
		properties.write(player.getAppearance().getGender().getCode());

		// headicon
		properties.write(player.getHeadIcon());

		// these two custom remove this if you want to use a #317 client
		properties.write(-1);
		properties.write(-1);

		// pnpc
		if (player.getId() == -1) {

		   // helm or hat
			if (player.getEquipment().get(Equipment.HEAD_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.HEAD_SLOT).getId());
			} else {
				properties.write(0);
			}
				
				// cape
			if (player.getEquipment().get(Equipment.CAPE_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.CAPE_SLOT).getId());
			} else {
				properties.write(0);
			}

				// amulet
			if (player.getEquipment().get(Equipment.NECKLACE_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.NECKLACE_SLOT).getId());
			} else {
				properties.write(0);
			}

				//weapon

			if (player.getEquipment().get(Equipment.WEAPON_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.WEAPON_SLOT).getId());
			} else {
				properties.write(0);
			}

				// torso
			if (player.getEquipment().get(Equipment.CHEST_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.CHEST_SLOT).getId());
			} else {
				properties.writeShort(0x100 + player.getAppearance().getTorso());
			}

				// shield
			if (player.getEquipment().get(Equipment.SHIELD_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.SHIELD_SLOT).getId());
			} else {
				properties.write(0);
			}
				
				// full body
			if (player.getEquipment().get(Equipment.CHEST_SLOT) != null) {
				if (!Equipment.isFullBody(player.getEquipment().get(Equipment.CHEST_SLOT).getId())) {
					properties.writeShort(0x100 + player.getAppearance().getArms());
				} else {
					properties.write(0);
				}
			} else {
				properties.writeShort(0x100 + player.getAppearance().getArms());
			}
				
				// legs
			if (player.getEquipment().get(Equipment.LEGS_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.LEGS_SLOT).getId());
			} else {
				properties.writeShort(0x100 + player.getAppearance().getLegs());
			}
				
				// full helm
				if (player.getEquipment().get(Equipment.HEAD_SLOT) != null) {
					if (!Equipment.isFullHat(player.getEquipment().get(Equipment.HEAD_SLOT).getId())) {
						properties.writeShort(0x100 + player.getAppearance().getHead());
					} else {
						properties.write(0);
					}
				} else {
					properties.writeShort(0x100 + player.getAppearance().getHead());
				}
				
				// hands
			if (player.getEquipment().get(Equipment.HANDS_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.HANDS_SLOT).getId());
			} else {
				properties.writeShort(0x100 + player.getAppearance().getHands());
			}
				
				// feet
			if (player.getEquipment().get(Equipment.FEET_SLOT) != null) {
				properties.writeShort(0x200 + player.getEquipment().get(Equipment.FEET_SLOT).getId());
			} else {
				properties.writeShort(0x100 + player.getAppearance().getFeet());
			}

				// beard
			if (player.getEquipment().get(Equipment.HEAD_SLOT) != null) {
				if (Equipment.isFullHat(player.getEquipment().get(Equipment.HEAD_SLOT).getId())) {
					properties.writeShort(0x100 + player.getAppearance().getJaw());
				} else {
					properties.write(0);
				}
			} else {
				if (player.getAppearance().getGender() == Appearance.Gender.MALE) {
					properties.writeShort(0x100 + player.getAppearance().getJaw());
				} else {
					properties.write(0);
				}
			}
		} else {
			properties.writeShort(-1);
			properties.writeShort(player.getId());
		}
		properties.write(player.getAppearance().getHairColor());
		properties.write(player.getAppearance().getTorsoColor());
		properties.write(player.getAppearance().getLegsColor());
		properties.write(player.getAppearance().getFeetColor());
		properties.write(player.getAppearance().getSkinColor());
		properties.writeShort(player.getMobAnimation().getStand());
		properties.writeShort(player.getMobAnimation().getTurn());
		properties.writeShort(player.getMobAnimation().getWalk());
		properties.writeShort(player.getMobAnimation().getTurn180());
		properties.writeShort(player.getMobAnimation().getTurn90CW());
		properties.writeShort(player.getMobAnimation().getTurn90CCW());
		properties.writeShort(player.getMobAnimation().getRun());
		properties.writeLong(StringUtils.stringToLong(player.getUsername()));
		properties.writeString(Integer.toHexString(0));
		properties.writeString("");
		properties.write(player.getCombatLevel());
		properties.writeShort(0);
		builder.write(properties.buffer().writerIndex(), ByteModification.NEGATION);
		builder.writeBytes(properties.buffer());
	}

}
