package rs2.game.model.entity.mob.combat;

import rs2.game.model.entity.mob.combat.type.CombatType;

public final class CombatTimer {

    private int meleeCooldown;
    private int rangeCooldown;
    private int magicCooldown;

    public boolean hasCooldown(CombatType type) {
        int diff = 4;
        if (magicCooldown < diff) {
            boolean magic = type != CombatType.MELEE && type != CombatType.RANGE;

            if (magic) {
                return false;
            }
        }

        if (meleeCooldown < diff) {

            if (type == CombatType.MELEE) {
                return false;
            }
        }

        if (rangeCooldown < diff) {

            if (type == CombatType.RANGE) {
                return false;
            }
        }

        return true;
    }

    public boolean cooldownTick(CombatType type) {
        boolean cooldown = false;
        if (magicCooldown >= 1) {
            magicCooldown--;

            boolean magic = type != CombatType.MELEE && type != CombatType.RANGE;

            if (magic && magicCooldown >= 1) {
                cooldown = true;
            }
        }

        if (meleeCooldown >= 1) {
            meleeCooldown--;

            if (type == CombatType.MELEE && meleeCooldown >= 1) {
                cooldown = true;
            }
        }

        if (rangeCooldown >= 1) {
            rangeCooldown--;

            if (type == CombatType.RANGE && rangeCooldown >= 1) {
                cooldown = true;
            }
        }

        return cooldown;
    }

    public void setCooldown(CombatType type, int delay) {
        switch (type) {
            case MELEE:
                meleeCooldown = delay;

                if (magicCooldown < 2) {
                    magicCooldown = 2;
                }

                if (rangeCooldown < 3) {
                    rangeCooldown = 3;
                }
                break;
            case RANGE:
                rangeCooldown = delay;

                if (meleeCooldown < 4) {
                    meleeCooldown = 4;
                }

                if (magicCooldown < 3) {
                    magicCooldown = 3;
                }
                break;
            default:
                magicCooldown = delay;

                if (meleeCooldown < 4) {
                    meleeCooldown = 4;
                }

                if (rangeCooldown < 3) {
                    rangeCooldown = 3;
                }
                break;
        }
    }

    public void setMagicCooldown(byte magicCooldown) {
        this.magicCooldown = magicCooldown;
    }

    public void setMeleeCooldown(byte meleeCooldown) {
        this.meleeCooldown = meleeCooldown;
    }

    public void setRangeCooldown(byte rangeCooldown) {
        this.rangeCooldown = rangeCooldown;
    }
}
