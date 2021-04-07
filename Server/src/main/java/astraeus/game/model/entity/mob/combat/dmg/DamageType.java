package astraeus.game.model.entity.mob.combat.dmg;

import astraeus.game.model.entity.mob.combat.type.CombatType;

/**
 * Represents the enumerated types of damages that can be performed.
 *
 * @author Vult-R
 */
public enum DamageType {
    NONE(-1),
    MELEE(0),
    RANGED(1),
    MAGIC(2),
    DEFLECT(3),
    CANNON(4);

    private int id;

    private DamageType(int id) {
        this.id = id;
    }
    
	public static DamageType of(CombatType type) {
		switch (type) {
		case MAGIC:
			return MAGIC;
		case MELEE:
			return MELEE;
		case RANGE:
			return RANGED;
		}
		return NONE;
	}

    public int getId() {
        return id;
    }

}
