package rs2.game.model.entity.mob.combat.type;

/**
 * Represents the enumerated types of combat
 *
 * @author Vult-R
 */
public enum CombatType {

    MELEE(4),
    RANGE(4),
    MAGIC(2);

    private int rate;

    private CombatType(int rate) {
        this.rate = rate;
    }

    public int getRate() {
        return rate;
    }

}
