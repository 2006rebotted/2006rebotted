package astraeus.game.model.entity.mob.combat.type.range;

public class RangedWeaponDefinition {
    private final AmmoType type;
    private final int[] arrowsAllowed;

    public RangedWeaponDefinition(int[] allowed, AmmoType type) {
        this.type = type;
        this.arrowsAllowed = allowed;
    }

    public int[] getArrowsAllowed() {
        return arrowsAllowed;
    }

    public AmmoType getType() {
        return type;
    }

}
