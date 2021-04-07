package rs2.game.model.sound;

/**
 * An enumeration of the volume levels in-game.
 *
 * @author Seven
 */
public enum Volume {

    SILENT(4),

    QUIET(3),

    NORMAL(2),

    HIGH(1),

    LOUD(0);

    private final int code;

    private Volume(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
