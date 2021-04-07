package astraeus.game.model.entity.mob.combat;

import astraeus.game.model.entity.mob.player.collect.Equipment;

/**
 * Represents an attack style.
 *
 * @author Vult-R
 */
public enum AttackStyle {

    STAB(Equipment.STAB, Equipment.STAB_DEFENSE),

    SLASH(Equipment.SLASH, Equipment.SLASH_DEFENSE),

    CRUSH(Equipment.CRUSH, Equipment.CRUSH_DEFENSE),

    RANGED(Equipment.RANGED, Equipment.RANGED_DEFENSE),

    MAGIC(Equipment.MAGIC, Equipment.MAGIC_DEFENSE);

    private int offensiveSlot;
    private int defensiveSlot;

    private AttackStyle(int offensiveSlot, int defensiveSlot) {
        this.offensiveSlot = offensiveSlot;
        this.defensiveSlot = defensiveSlot;
    }

    public int getDefensiveSlot() {
        return defensiveSlot;
    }

    public int getOffensiveSlot() {
        return offensiveSlot;
    }

}
