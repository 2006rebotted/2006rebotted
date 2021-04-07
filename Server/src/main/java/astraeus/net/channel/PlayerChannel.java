package astraeus.net.channel;

import astraeus.Server;
import astraeus.game.model.World;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.io.PlayerDeserializer;
import astraeus.net.packet.Sendable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import astraeus.net.NetworkConstants;
import astraeus.net.codec.game.GamePacketDecoder;
import astraeus.net.codec.game.GamePacketEncoder;
import astraeus.net.codec.login.LoginDetailsPacket;
import astraeus.net.codec.login.LoginResponse;
import astraeus.net.codec.login.LoginResponsePacket;
import astraeus.net.packet.IncomingPacket;
import astraeus.net.packet.IncomingPacketHandlerRegistration;
import astraeus.net.packet.OutgoingPacket;
import astraeus.net.packet.out.SetPlayerSlotPacket;
import io.netty.channel.socket.SocketChannel;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Represents a {@link Channel} that provides asynchronous I/O operations for a
 * {@link Player}.
 * 
 * @author Seven <https://github.com/vult-r>
 */
public class PlayerChannel {

	/**
	 * The single logger for this class.
	 */
	public static final Logger logger = Logger.getLogger(PlayerChannel.class.getName());

	/**
	 * The prioritized packets that will be handled on the next sequence.
	 */
	private final Queue<IncomingPacket> prioritizedPackets = new ConcurrentLinkedQueue<>();

	/**
	 * The incoming packets that will be handled on the next sequence.
	 */
	public final Queue<IncomingPacket> incomingPackets = new ConcurrentLinkedQueue<>();

	/**
	 * The channel that will manage the connection for this player.
	 */
	private Channel channel;

	/**
	 * The player that this operation will be executed for.
	 */
	private Player player;

	/**
	 * The address of this user connected by this channel.
	 */
	private final String hostAddress;

	/**
	 * Creates a new {@link PlayerChannel}
	 * 
	 * @param channel
	 *            the channel that data will be written to.
	 */
	public PlayerChannel(SocketChannel channel) {
		this.channel = channel;
		this.player = new Player(this);
		this.hostAddress = channel.remoteAddress().getAddress().getHostAddress();
	}

	/**
	 * Processes a connection into an actual player.
	 *
	 * @param packet
	 *            The packet containing all the users login information.
	 */
	public void handleUserLoginDetails(LoginDetailsPacket packet) {

		SocketChannel channel = (SocketChannel) packet.getContext().channel();

		final String username = packet.getUsername();

		final String password = packet.getPassword();

		@SuppressWarnings("unused")
		final String uuid = packet.getUUID();

		@SuppressWarnings("unused")
		final String hostAddress = channel.remoteAddress().getAddress().getHostAddress();

		Player player = this.getPlayer();

		player.setUsername(username);

		player.setPassword(password);

		try {
			player.attr().put(Player.NEW_PLAYER_KEY, !PlayerDeserializer.deserialize(player));
		} catch (Exception e) {
			e.printStackTrace();
		}

		LoginResponse response = evaluate(packet);

		ChannelFuture future = channel.writeAndFlush(new LoginResponsePacket(response, player.getRights(), false));

		if (response != LoginResponse.NORMAL) {
			future.addListener(ChannelFutureListener.CLOSE);
			return;
		}

		future.awaitUninterruptibly();

		player.queuePacket(new SetPlayerSlotPacket());

		channel.pipeline().replace("login-encoder", "game-encoder", new GamePacketEncoder(packet.getEncryptor()));
		channel.pipeline().replace("login-decoder", "game-decoder", new GamePacketDecoder(packet.getDecryptor()));

		World.world.queueLogin(player);
	}

	/**
	 * Evaluates the user logging in, and sends the appropriate response code
	 * indicating their status.
	 *
	 * @param packet
	 *            The packet containing their login information.
	 */
	private final LoginResponse evaluate(LoginDetailsPacket packet) {

		// username too long or too short
		if (packet.getUsername().isEmpty() || packet.getUsername().length() >= 13) {
			return LoginResponse.INVALID_CREDENTIALS;
		}

		// username contains invalid characters
		if (!packet.getUsername().matches("^[a-zA-Z0-9 ]{1,12}$")) {
			return LoginResponse.INVALID_CREDENTIALS;
		}

		// there is no password or password is too long
		if (packet.getPassword().isEmpty() || packet.getPassword().length() >= 20) {
			return LoginResponse.INVALID_CREDENTIALS;
		}

		// password does not match password on file.
		if (!packet.getPassword().equals(player.getPassword())) {
			return LoginResponse.INVALID_CREDENTIALS;
		}

		// the world is currently full
		if (World.world.getPlayers().isFull()) {
			return LoginResponse.WORLD_FULL;
		}

		// this user is already online
		if (World.world.getPlayers().contains(player)) {
			return LoginResponse.ACCOUNT_ONLINE;
		}

		// the users ip address has been banned.
		if (World.world.getIpBans().contains(hostAddress)) {
			return LoginResponse.ACCOUNT_DISABLED;
		}

		// the users computer has been banned
		if (World.world.getBannedUUIDs().contains(packet.getUUID())) {
			return LoginResponse.ACCOUNT_DISABLED;
		}

		// check the uuid when the player first created their account.
		if (World.world.getBannedUUIDs().contains(player.getUUID())) {
			return LoginResponse.ACCOUNT_DISABLED;
		}

		// prevents users from logging in before the network has been fully
		// bound
		if (!Server.serverStarted) {
			return LoginResponse.SERVER_BEING_UPDATED;
		}

		// everything went good
		return LoginResponse.NORMAL;
	}

	/**
	 * Flushes all outgoing packets to be send to the client for this sequence.
	 * 
	 * @note player update packet and npc update packet should not use this.
	 * 
	 * @param out
	 *            The outgoing packet that will be sent.
	 */
	public void flush() {
		try {
			channel.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Processes a packet immediately to be sent to the client.
	 * 
	 * @param out
	 * 		The packet to send.
	 */
	public void flush(Sendable out) {
		try {
			Optional<OutgoingPacket> packet = out.writePacket(player);

			packet.ifPresent(channel::writeAndFlush);
		} catch (Exception ex) {

		}
	}

	/**
	 * Queues a packet to be processed at the end of player synchronization.
	 * 
	 * @param out
	 * 		The packet to queue.
	 */
	public void queue(Sendable out) {
		Optional<OutgoingPacket> packet = out.writePacket(player);
		
		packet.ifPresent(channel::write);
	}

	/**
	 * Gets the channel that will manage the connection for this player.
	 * 
	 * @return the channel for this player.
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Gets the player for this channel.
	 * 
	 * @return the player for this channel.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Prioritizes incoming packets for a player.
	 */
	public void handlePrioritizedPacketQueue() {
		IncomingPacket packet;
		while ((packet = prioritizedPackets.poll()) != null) {
			if (player != null) {
				IncomingPacketHandlerRegistration.dispatchToHandler(player, packet);
			}
		}
	}

	/**
	 * Handles incoming queued packets for a player.
	 */
	public void handleQueuedPackets() {
		handlePrioritizedPacketQueue();
		IncomingPacket packet;
		while ((packet = incomingPackets.poll()) != null) {
			if (packet.getOpcode() > 0) {
				if (player != null) {
					IncomingPacketHandlerRegistration.dispatchToHandler(player, packet);
				}
			}
		}
	}

	/**
	 * Handles an upstream {@code packet}.
	 * 
	 * @param msg
	 *            The packet to handle.
	 */
	public void handleIncomingPacket(Object msg) {
		if (msg instanceof LoginDetailsPacket) {
			handleUserLoginDetails((LoginDetailsPacket) msg);
		} else if (msg instanceof IncomingPacket) {
			queueIncomingPacket((IncomingPacket) msg);
		}
	}

	/**
	 * Queues {@link IncomingPacket}s to be handled on the next sequence.
	 * 
	 * @param packet
	 *            The packet to queue.
	 */
	public void queueIncomingPacket(final IncomingPacket packet) {
		if (incomingPackets.size() <= NetworkConstants.PACKET_LIMIT) {
			if (packet.isPrioritized()) {
				prioritizedPackets.add(packet);
			} else {
				incomingPackets.add(packet);
			}
		}
	}

	/**
	 * Gets the host of this channel
	 */
	public String getHostAddress() {
		return hostAddress;
	}

}
