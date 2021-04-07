package rs2.game.model.entity.mob.player;

public enum PlayerRights {

	PLAYER(0, 0),

	MEMBER(1, 0),

	MODERATOR(2, 1),

	ADMINISTRATOR(3, 2),

	DEVELOPER(4, 2);

	private final int protocolValue;

	private final int values;

	private PlayerRights(int values, int protocalValue) {
		this.protocolValue = protocalValue;
		this.values = values;
	}

	public final boolean equal(PlayerRights other) {
		return values == other.values;
	}

	public final int getProtocolValue() {
		return protocolValue;
	}

	public final int getValues() {
		return values;
	}

	public final boolean greater(PlayerRights other) {
		return values > other.values;
	}

	public final boolean greaterOrEqual(PlayerRights other) {
		return values >= other.values;
	}

	public final boolean less(PlayerRights other) {
		return values < other.values;
	}

	public final boolean lessOrEqual(PlayerRights other) {
		return values <= other.values;
	}
}
