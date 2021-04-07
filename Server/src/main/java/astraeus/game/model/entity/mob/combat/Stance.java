package astraeus.game.model.entity.mob.combat;

/**
 * Represents a stance in combat.
 *
 * @author Vult-R
 */
public enum Stance {

    /**
     * The accurate stance.
     */
    ACCURATE(3),

    /**
     * The aggressive stance.
     */
    AGGRESSIVE(0),

    /**
     * The defensive stance.
     */
    DEFENSIVE(0),

    /**
     * The controlled stance
     */
    CONTROLLED(1);

    /**
     * The accuracy bonus of the stance.
     */
    private final int accuracyIncrease;

    /**
     * Constructs a combat stance.
     *
     * @param accuracyIncrease
     *            The accuracy bonus of the stance.
     */
    Stance(int accuracyIncrease) {
        this.accuracyIncrease = accuracyIncrease;
    }

    /**
     * Gets the accuracy bonus of the stance.
     *
     * @return The accuracy bonus.
     */
    public int getAccuracyIncrease() {
        return accuracyIncrease;
    }
}
