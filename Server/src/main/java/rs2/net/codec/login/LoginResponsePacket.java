package rs2.net.codec.login;

import rs2.game.model.entity.mob.player.PlayerRights;

/**
 * The packet that contains information about a players login attempt.
 * 
 * @author Vult-R
 */
public final class LoginResponsePacket {

	/**
	 * The login response that was indicated.
	 */
    private final LoginResponse response;

    /**
     * The rights of the player logging in.
     */
    private final PlayerRights rights;

    /**
     * The flag that denotes this player is flagged.
     */
    private final boolean flagged;

    /**
     * Creates a new {@link LoginResponsePacket}.
     * 
     * @param response
     * 		The response that was indicated.
     * 
     * @param rights
     * 		The rights of the player logging in.
     * 
     * @param flagged
     * 		The flag that indicates a player was flagged.
     */
    public LoginResponsePacket(LoginResponse response, PlayerRights rights, boolean flagged) {
        this.response = response;
        this.rights = rights;
        this.flagged = flagged;
    }

    public LoginResponsePacket(LoginResponse response) {
        this(response, PlayerRights.PLAYER, false);
    }

    public LoginResponse getResponse() {
        return response;
    }

    public PlayerRights getRights() {
        return rights;
    }

    public boolean isFlagged() {
        return flagged;
    }
}

