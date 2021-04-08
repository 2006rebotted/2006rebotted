package rs2.net.packet.out;

import rs2.game.model.Position;
import rs2.game.model.Projectile;
import rs2.game.model.entity.mob.player.Player;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.Sendable;

import java.util.Optional;

public final class DisplayProjectilePacket implements Sendable {

	private final Projectile projectile;
	private final int lock;
	private final byte offsetX;
	private final byte offsetY;
	//private final SendAltCoordinates sendAlt;

	public DisplayProjectilePacket(Player player, Projectile projectile, Position location, int lock, byte offsetX, byte offsetY) {
		this.projectile = projectile;
		this.lock = lock;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		if (projectile.getSize() > 1) {
			//sendAlt = new SendAltCoordinates(new Location(location.getX() + (projectile.getSize() / 2), location.getY() + (projectile.getSize() / 2)));
		} else {
			//sendAlt = new SendAltCoordinates(location);
		}
	}

	@Override
	public Optional<OutgoingPacket> writePacket(Player player) {
		//player.send(sendAlt);
		GamePacketBuilder builder = new GamePacketBuilder(117);
		builder.write(50)
		.write(offsetX)
		.write(offsetY)
		.writeShort(lock)
		.writeShort(projectile.getId())
		.write(projectile.getStartHeight())
		.write(projectile.getEndHeight())
		.writeShort(projectile.getDelay())
		.writeShort(projectile.getDuration())
		.write(projectile.getCurve())
		.write(64);
		return builder.toOutgoingPacket();
	}

}