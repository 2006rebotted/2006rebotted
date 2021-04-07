package astraeus.net.codec.login;

/**
 * Represents the enumerated login response codes.
 * 
 * @author Vult-R
 */
public enum LoginResponse {
	EXCHANGE_DATA(0),
	DELAY(1),
    NORMAL(2),
    INVALID_CREDENTIALS(3),
    ACCOUNT_DISABLED(4),
    ACCOUNT_ONLINE(5),
    GAME_UPDATED(6),
    WORLD_FULL(7),
    LOGIN_SERVER_OFFLINE(8),
    LOGIN_LIMIT_EXCEEDED(9),
    BAD_SESSION_ID(10),
    LOGIN_SERVER_REJECTED_SESSION(11),
    MEMBERS_ACCOUNT_REQUIRED(12),
    COULD_NOT_COMPLETE_LOGIN(13),
    SERVER_BEING_UPDATED(14),
    RECONNECTING(15),
    LOGIN_ATTEMPTS_EXCEEDED(16),
    MEMBERS_ONLY_AREA(17),
    INVALID_LOGIN_SERVER(20),
    TRANSFERING_PROFILE(21);

    /**
     * The response code.
     */
    private final int opcode;

    /**
     * Creates a new {@link LoginResponse}.
     *
     * @param opcode
     * 		The response code.
     */
    LoginResponse(int opcode) {
        this.opcode = opcode;
    }

    /**
     * Gets the opcode for this response.
     * 
     * @return The response code.
     */
    public final int getOpcode() {
        return opcode;
    }
}

