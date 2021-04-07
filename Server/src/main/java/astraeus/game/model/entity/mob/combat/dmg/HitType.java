package astraeus.game.model.entity.mob.combat.dmg;

/**
 * Represents a type of hit splat
 *
 * @author Vult-R
 */
public enum HitType {
    BLOCKED(0),
    NORMAL(1),
    POISON(2),
    VENOM(3),
    CRITICAL(4);

    private int id;

    private HitType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
