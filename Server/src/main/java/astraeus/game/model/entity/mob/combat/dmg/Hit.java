package astraeus.game.model.entity.mob.combat.dmg;

/**
 * Represents a hitsplat
 *
 * @author Vult-R
 */
public final class Hit {

    private int damage;

    private HitType type;

    private DamageType damageType;

    private boolean isSecond;
    private boolean enabled;

    public Hit(int damage) {
        this(damage, HitType.NORMAL, DamageType.NONE);
    }

    public Hit(int damage, DamageType damageType) {
        this(damage, HitType.NORMAL, damageType);
    }

    public Hit(int damage, HitType type, DamageType damageType) {
        this.damage = damage;
        this.type = type;
        this.damageType = damageType;
        if (this.damage == 0 && this.type == HitType.NORMAL) {
            this.type = HitType.BLOCKED;
        } else if (this.damage > 0 && this.type == HitType.BLOCKED) {
            this.damage = 0;
        } else if (this.damage < 0) {
            this.damage = 0;
        }
        enable();
    }

    public int getDamage() {
        return damage;
    }

    public HitType getType() {
        return type;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    public boolean isSecond() {
        return isSecond;
    }

    public void setSecond(boolean isSecond) {
        this.isSecond = isSecond;
    }

    public void setDamage(int damage) {
        this.damage = damage;

        if (damage == 0) {
            type = HitType.BLOCKED;
        } else if (damage < 0) {
            disable();
        }
    }

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public Hit clone() {
        return new Hit(damage, type, damageType);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Hit.class) {
            return false;
        }

        final Hit hit = (Hit) other;
        return hit.damage == damage && hit.type == type;
    }

    @Override
    public String toString() {
        return "[HIT] - Damage: " + getDamage() + " Type: " + getType().name();
    }

}

