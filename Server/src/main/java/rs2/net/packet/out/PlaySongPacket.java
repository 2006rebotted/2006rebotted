package rs2.net.packet.out;

import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.ByteOrder;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

/**
 * The {@link OutgoingPacket} that plays a song.
 * 
 * @author SeVen
 */
public final class PlaySongPacket implements Sendable {

	/**
	 * The id of the song.
	 */
	private final int id;

	/**
	 * Creates a new {@link PlaySongPacket}.
	 * 
	 * @param id
	 *            The id of the song.
	 */
	public PlaySongPacket(int id) {
		this.id = id;
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		GamePacketBuilder builder = new GamePacketBuilder(74);
		if (!player.attr().get(Player.MUSIC_KEY) || id == -1) {
			return Optional.empty();
		}
		builder.writeShort(id, ByteOrder.LITTLE);
		return builder.toOutgoingPacket();
	}

}
