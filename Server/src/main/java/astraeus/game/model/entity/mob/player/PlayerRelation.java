package astraeus.game.model.entity.mob.player;

import astraeus.game.model.World;
import astraeus.net.packet.out.*;
import astraeus.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The class that resembles a relation between players.
 *
 * @author SeVen
 */
public class PlayerRelation {

	/**
	 * The enumerated types for private message list statuses.
	 */
	public enum PrivateMessageListStatus {
		LOADING(0),

		CONNECTED(1),

		LOADED(2);

		private final int code;

		PrivateMessageListStatus(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

	}

	/**
	 * The enumeration of a players private messaging policy.
	 */
	public enum PrivateMessagePolicy {
		/**
		 * This setting allows for everyone to see this user's online status.
		 */
		ALLOW,

		/**
		 * This setting only allows friends to see this user's online status.
		 */
		FRIENDS_ONLY,

		/**
		 * This setting hides this user's online status to all players.
		 */
		BLOCK
	}

	/**
	 * Handles a players split chat setting
	 */
	public void handleSplitChat() {
		player.attr().put(Player.SPLIT_CHAT_KEY, !player.attr().get(Player.SPLIT_CHAT_KEY));
		player.queuePacket(new SetWidgetConfigPacket(287, player.attr().get(Player.SPLIT_CHAT_KEY) ? 1 : 0)); // chat
	}

	/**
	 * The default setting for private chat settings.
	 */
	private PrivateMessagePolicy status = PrivateMessagePolicy.ALLOW;

	/**
	 * The list of friends this player has.
	 */
	private List<Long> friendList = new ArrayList<Long>(200);

	/**
	 * The list of ignored players this player has.
	 */
	private List<Long> ignoreList = new ArrayList<Long>(100);

	/**
	 * The associated player of this relation.
	 */
	private final Player player;

	/**
	 * Creates a new {@link PlayerRelation}.
	 *
	 * @param player
	 *            The player being associated.
	 */
	public PlayerRelation(Player player) {
		this.player = player;
	}

	public void addFriend(long username) {
		final String name = StringUtils.longToString(username);

		if (friendList.size() >= 200) {
			player.queuePacket(new ServerMessagePacket("Your friend list is full!"));
			return;
		}

		if (ignoreList.contains(username)) {
			player.queuePacket(new ServerMessagePacket(String.format("Please remove %s from your ignore list.", name)));
			return;
		}

		if (friendList.contains(username)) {
			player.queuePacket(new ServerMessagePacket(String.format("You already have %s on your friends list.", name)));
			return;
		}

		friendList.add(username);
		updateLists(true);

		if (World.world.searchPlayer(name).isPresent()) {
			final Optional<Player> friend = Optional.ofNullable(World.world.searchPlayer(name).get());
			friend.ifPresent($it -> $it.getRelation().updateLists(true));
		}

	}

	public void addIgnore(long username) {
		final String name = StringUtils.longToString(username);

		if (ignoreList.size() >= 100) {
			player.queuePacket(new ServerMessagePacket("Your ignore list is full!"));
			return;
		}

		if (friendList.contains(username)) {
			player.queuePacket(new ServerMessagePacket(String.format("Please remove %s from your friend list first.", name)));
			return;
		}

		if (ignoreList.contains(username)) {
			player.queuePacket(new ServerMessagePacket("This user is already on your ignore list."));
			return;
		}

		ignoreList.add(username);
		player.queuePacket(new AddIgnorePacket());
		updateLists(true);

		if (World.world.searchPlayer(name).isPresent()) {
			final Player ignored = World.world.searchPlayer(name).get();
			ignored.getRelation().updateLists(false);
		}

	}

	public List<Long> getFriendList() {
		return friendList;
	}

	public List<Long> getIgnoreList() {
		return ignoreList;
	}

	public Player getPlayer() {
		return player;
	}

	public PrivateMessagePolicy getStatus() {
		return status;
	}

	public void removeFriend(long username) {
		if (friendList.contains(username)) {
			friendList.remove(username);

			if (World.world.searchPlayer(StringUtils.longToString(username)).isPresent()) {
				final Optional<Player> unfriend = Optional.ofNullable(World.world.searchPlayer(StringUtils.longToString(username)).get());

				unfriend.ifPresent($it -> $it.getRelation().updateLists($it.attr().get(Player.ACTIVE_KEY)));

				updateLists(player.attr().get(Player.ACTIVE_KEY));
			}
		} else {
			player.queuePacket(new ServerMessagePacket("This player is not on your friends list!"));
		}

	}

	public void removeIgnore(long username) {
		System.out.println("player is removing ignore");
		if (ignoreList.contains(username)) {
			ignoreList.remove(username);

			updateLists(true);

			if (status == PrivateMessagePolicy.ALLOW) {
				if (World.world.searchPlayer(StringUtils.longToString(username)).isPresent()) {
					final Player ignored = World.world.searchPlayer(StringUtils.longToString(username)).get();
					ignored.getRelation().updateLists(true);
				}
			}
		} else {
			player.queuePacket(new ServerMessagePacket("This player is not on your ignored list!"));
		}
	}

	public void sendFriends() {
		for (int index = 0; index < player.getRelation().getFriendList().size(); index++) {
			player.queuePacket(new AddFriendPacket(player.getRelation().getFriendList().get(index), 0));
		}
	}

	public void sendPrivateMessage(long to, byte[] message, int size) {
		Optional<Player> search = World.world.searchPlayer(StringUtils.longToString(to).replaceAll("_",  " "));

		if (!search.isPresent()) {
			player.queuePacket(new ServerMessagePacket("This player is currently offline."));
			return;
		}

		Player friend = search.get();

		if (friend.getRelation().getStatus() == PrivateMessagePolicy.FRIENDS_ONLY
				&& !friend.getRelation().getFriendList().contains(player.getLongUsername())) {
			this.player.queuePacket(new ServerMessagePacket("This player is currently offline."));
			return;
		}

		if (status == PrivateMessagePolicy.BLOCK) {
			setChatPolicy(PrivateMessagePolicy.FRIENDS_ONLY, true);
		}

		friend.queuePacket(new SendPrivateMessagePacket(player.getLongUsername(), player.getRights(), message, size));
	}

	public void setChatPolicy(PrivateMessagePolicy policy, boolean update) {
		this.status = policy;
		if (update) {
			updateLists(true);
		}
	}

	public void setPrivateChatSetting(PrivateMessagePolicy privateChatSetting) {
		this.status = privateChatSetting;
	}

	public void updateLists(boolean online) {
		if (status == PrivateMessagePolicy.BLOCK) {
			online = false;
		}

		player.queuePacket(new SetFriendListStatusPacket(PrivateMessageListStatus.LOADED));

		for (final Player other : World.world.getPlayers()) {

			if (other == null) {
				continue;
			}

			boolean tempOnlineStatus = online;

			if (other.getRelation().friendList.contains(player.getLongUsername())) {
				if (status == PrivateMessagePolicy.FRIENDS_ONLY && !friendList.contains(other.getLongUsername())
						|| status == PrivateMessagePolicy.BLOCK || ignoreList.contains(other.getLongUsername())) {
					tempOnlineStatus = false;
				}
				other.queuePacket(new AddFriendPacket(player.getLongUsername(), tempOnlineStatus ? 1 : 0));
			}
			boolean tempOn = true;

			if (player.getRelation().getFriendList().contains(other.getLongUsername())) {
				if (other.getRelation().getStatus() == PrivateMessagePolicy.BLOCK
						|| other.getRelation().getStatus() == PrivateMessagePolicy.FRIENDS_ONLY
						&& !other.getRelation().getFriendList().contains(player.getLongUsername())) {
					tempOn = false;
				}
				player.queuePacket(new AddFriendPacket(other.getLongUsername(), tempOn ? 1 : 0));
			}

		}

	}

	/**
	 * @param friendList the friendList to set
	 */
	public void setFriendList(List<Long> friendList) {
		this.friendList = friendList;
	}

	/**
	 * @param ignoreList the ignoreList to set
	 */
	public void setIgnoreList(List<Long> ignoreList) {
		this.ignoreList = ignoreList;
	}



}
