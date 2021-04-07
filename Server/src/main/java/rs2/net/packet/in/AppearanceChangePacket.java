package rs2.net.packet.in;

import rs2.game.model.entity.mob.player.Appearance;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.PlayerRights;
import rs2.net.codec.game.ByteBufReader;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.Receivable;
import rs2.net.packet.out.RemoveWidgetPacket;
import rs2.net.packet.out.ServerMessagePacket;

/**
 * The {@link IncomingPacket} responsible for changing a players appearance.
 * 
 * @author SeVen
 */
@IncomingPacket.IncomingPacketOpcode(IncomingPacket.CHARACTER_DESIGN)
public class AppearanceChangePacket implements Receivable {

	/**
	 * Determines if a player chose valid appearance values.
	 * 
	 * @param player
	 *            The player to check.
	 * 
	 * @param appearance
	 *            The appearance values to check.
	 * 
	 * @return {@code true} If the player has valid appearance values.
	 *         {@code false} otherwise.
	 */
	public static boolean isValid(Player player, Appearance appearance) {
		switch (appearance.getGender()) {

		case MALE:

			if (!(appearance.getHead() >= 0 && appearance.getHead() <= 8)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid head id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid head id."));
				return false;
			}

			if (!(appearance.getJaw() >= 10 && appearance.getJaw() <= 17)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid jaw id %d", appearance.getJaw()) : "You cannot change your appearance, because you have an invalid jaw id."));
				return false;
			}

			if (!(appearance.getTorso() >= 18 && appearance.getTorso() <= 25)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid torso id %d", appearance.getTorso()) : "You cannot change your appearance, because you have an invalid torso id."));
				return false;
			}

			if (!(appearance.getArms() >= 26 && appearance.getArms() <= 31)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid arm id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid arm id."));
				return false;
			}

			if (!(appearance.getHands() >= 33 && appearance.getHands() <= 34)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid hand id %d", appearance.getHands()) : "You cannot change your appearance, because you have an invalid hand id."));
				return false;
			}

			if (!(appearance.getLegs() >= 36 && appearance.getLegs() <= 40)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid leg id %d", appearance.getLegs()) : "You cannot change your appearance, because you have an invalid leg id."));
				return false;
			}

			if (!(appearance.getFeet() >= 42 && appearance.getFeet() <= 43)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid feet id %d", appearance.getFeet()) : "You cannot change your appearance, because you have an invalid feet id."));
				return false;
			}

			break;

		case FEMALE:

			if (!(appearance.getHead() >= 45 && appearance.getHead() <= 54)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid head id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid head id."));
				return false;
			}

			if (!(appearance.getJaw() == -1)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid jaw id %d", appearance.getJaw()) : "You cannot change your appearance, because you have an invalid jaw id."));
				return false;
			}

			if (!(appearance.getTorso() >= 56 && appearance.getTorso() <= 60)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid torso id %d", appearance.getTorso()) : "You cannot change your appearance, because you have an invalid torso id."));
				return false;
			}

			if (!(appearance.getArms() >= 61 && appearance.getArms() <= 65)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid arm id %d", appearance.getHead()) : "You cannot change your appearance, because you have an invalid arm id."));
				return false;
			}

			if (!(appearance.getHands() >= 67 && appearance.getHands() <= 68)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid hand id %d", appearance.getHands()) : "You cannot change your appearance, because you have an invalid hand id."));
				return false;
			}

			if (!(appearance.getLegs() >= 70 && appearance.getLegs() <= 77)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid leg id %d", appearance.getLegs()) : "You cannot change your appearance, because you have an invalid leg id."));
				return false;
			}

			if (!(appearance.getFeet() >= 79 && appearance.getFeet() <= 80)) {
				player.queuePacket(new ServerMessagePacket(player.getRights().greaterOrEqual(PlayerRights.ADMINISTRATOR) ? String.format("Cannot change appearance - Invalid feet id %d", appearance.getFeet()) : "You cannot change your appearance, because you have an invalid feet id."));
				return false;
			}

			break;

		}

		return true;
	}

	@Override
	public void handlePacket(Player player, IncomingPacket packet) {
		ByteBufReader reader = packet.getReader();

		final int gender = reader.readByte(false);
		final int head = reader.readByte(false);
		final int jaw = reader.readByte(false);
		final int torso = reader.readByte(false);
		final int arms = reader.readByte(false);
		final int hands = reader.readByte(false);
		final int legs = reader.readByte(false);
		final int feet = reader.readByte(false);

		final int hairColor = reader.readByte(false);
		final int torsoColor = reader.readByte(false);
		final int legsColor = reader.readByte(false);
		final int feetColor = reader.readByte(false);
		final int skinColor = reader.readByte(false);

		final Appearance appearance = new Appearance(gender == 0 ? Appearance.Gender.MALE : Appearance.Gender.FEMALE, head, gender == Appearance.Gender.MALE.getCode() ? jaw : -1, torso, arms, hands, legs, feet, hairColor, torsoColor, legsColor, feetColor, skinColor);

		if (AppearanceChangePacket.isValid(player, appearance)) {
			player.setAppearance(appearance);
		}

		if (player.attr().get(Player.DEBUG_KEY)) {
			player.queuePacket(new ServerMessagePacket(appearance.toString()));
		}

		player.queuePacket(new RemoveWidgetPacket());
	}

}
