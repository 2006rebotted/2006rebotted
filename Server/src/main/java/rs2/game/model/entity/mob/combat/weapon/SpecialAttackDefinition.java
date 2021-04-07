package rs2.game.model.entity.mob.combat.weapon;

/**
 * Represents a special attack definition
 *
 * @author Vult-R
 */
public class SpecialAttackDefinition {
    private final int layerId;
    private final int stringId;
    private final int buttonId;

    public SpecialAttackDefinition(int layerId, int stringId, int buttonId) {
        this.layerId = layerId;
        this.stringId = stringId;
        this.buttonId = buttonId;
    }

    public int getLayerId() {
        return layerId;
    }

    public int getStringId() {
        return stringId;
    }

    public int getButton() {
        return buttonId;
    }
}
