package rs2.game.model.entity.mob.combat.weapon;

import java.util.HashMap;
import java.util.Map;

import rs2.game.model.entity.mob.combat.type.CombatType;
import rs2.game.model.entity.mob.combat.type.range.RangedWeaponDefinition;

/**
 * The definition for all weapons.
 *
 * @author Vult-R
 */
public class WeaponDefinition {

    private static final Map<Integer, WeaponDefinition> definition = new HashMap<>();

    private final int id;
    private final String name;
    private WeaponInterface type;
    private final CombatType combatType;
    private final RangedWeaponDefinition rangedWeapon;
    private final boolean twoHanded;
    private final int blockAnimation;
    private final int standAnimation;
    private final int walkAnimation;
    private final int runAnimation;
    private final int attackSpeed;
    private final int[] animations;

    public WeaponDefinition(int id, String name, WeaponInterface type, CombatType combatType, RangedWeaponDefinition rangedWeaponDefinition, boolean twoHanded, int blockAnimation, int standAnimation, int walkAnimation, int runAnimation, int attackSpeed, int[] animations) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.combatType = combatType;
        this.rangedWeapon = rangedWeaponDefinition;
        this.twoHanded = twoHanded;
        this.blockAnimation = blockAnimation;
        this.standAnimation = standAnimation;
        this.walkAnimation = walkAnimation;
        this.runAnimation = runAnimation;
        this.attackSpeed = attackSpeed;
        this.animations = animations;
    }

    public static WeaponDefinition get(int id) {
        return definition.get(id);
    }

    /**
     * @return the weaponDefinitions
     */
    public static Map<Integer, WeaponDefinition> getWeaponDefinitions() {
        return definition;
    }

    public WeaponInterface getType() {
        return type;
    }

    public int[] getAnimations() {
        return animations;
    }

    /**
     * @return the attack speed
     */
    public int getAttackSpeed() {
        return attackSpeed;
    }

    /**
     * @return the blockAnimation
     */
    public int getBlockAnimation() {
        return blockAnimation;
    }

    /**
     * @return the attackType
     */
    public CombatType getCombatType() {
        return combatType;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    // /**
    // * @return the poisonous
    // */
    // public boolean isPoisonous() {
    // return poisonous;
    // }

    /**
     * @return the rangedWeapon
     */
    public RangedWeaponDefinition getRangedWeapon() {
        return rangedWeapon;
    }

    /**
     * @return the runAnimation
     */
    public int getRunAnimation() {
        return runAnimation;
    }

    /**
     * @return the standAnimation
     */
    public int getStandAnimation() {
        return standAnimation;
    }

    /**
     * @return the walkAnimation
     */
    public int getWalkAnimation() {
        return walkAnimation;
    }

    /**
     * @return the twoHanded
     */
    public boolean isTwoHanded() {
        return twoHanded;
    }
}
