package rs2.game.model.entity.mob.combat.weapon;

/**
 * Represents the enumerated types of weapons.
 *
 * @author Vult-R
 */
public enum WeaponInterface {
    UNARMED(5855, 5857, new SpecialAttackDefinition(7749, 7761, 7737)),
    THROWN(4446, 4449, new SpecialAttackDefinition(7649, 7661, 7637)),
    BOW(1764, 1767, new SpecialAttackDefinition(7549, 7561, 7548)),
    WHIP(12290, 12293, new SpecialAttackDefinition(12323, 12335, 12322)),
    WARHAMMER_OR_MAUL(425, 428, new SpecialAttackDefinition(7474, 7486, 7473)),
    SPEAR(4679, 4682, new SpecialAttackDefinition(7674, 7686, 7662)),
    STAFF(6103, 6132, new SpecialAttackDefinition(6117, 6129, 6104)),
    MAGIC_STAFF(328, 355, new SpecialAttackDefinition(18566, 18569, 340)),
    HALBERD(8460, 8463, new SpecialAttackDefinition(8493, 8505, 8481)),
    SWORD_OR_DAGGER(2276, 2279, new SpecialAttackDefinition(7574, 7586, 7562)),
    TWO_HANDED(4705, 4708, new SpecialAttackDefinition(7699, 7711, 7687)),
    LONGSWORD_OR_SCIMITAR(2423, 2426, new SpecialAttackDefinition(7599, 7611, 7587)),
    PICKAXE(5570, 5573, new SpecialAttackDefinition(7724, 7736, 7723)),
    BATTLEAXE(1698, 1701, new SpecialAttackDefinition(7499, 7591, 7498)),
    CLAWS(7762, 7765, new SpecialAttackDefinition(7800, 7812, 7788)),
    MACE(3796, 3799, new SpecialAttackDefinition(7624, 7636, 7623));

	/**
	 * The id of the interface that will be displayed.
	 */
    private final int interfaceId;
    
    /**
     * The id of the line that the weapon will be displayed on.
     */
    private final int nameLine;   
    
    /**
     * The definition for a special attack.
     */
    private final SpecialAttackDefinition special;

    private WeaponInterface(int interfaceId, int stringId, SpecialAttackDefinition special) {
        this.interfaceId = interfaceId;
        this.nameLine = stringId;
        this.special = special;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public int getStringId() {
        return nameLine;
    }

    public int getLayerId() {
        return special.getLayerId();
    }

    public int getSpecialStringId() {
        return special.getStringId();
    }

    public int getButton() {
        return special.getButton();
    }

    public int getSound() {
        return 0;
    }
}
