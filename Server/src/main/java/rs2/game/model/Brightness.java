package rs2.game.model;

/**
 * The enumeration of the brightness levels as seen by the client.
 *
 * @author Vult-R
 */
public enum Brightness {

    VERY_DARK(1),

    DARK(2),

    NORMAL(3),

    BRIGHT(4);

    private final int code;

    private Brightness(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
