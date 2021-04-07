package rs2.game.model.entity.mob.combat.dmg;

import rs2.game.model.entity.mob.Mob;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A queue of hits
 */
public class DamageQueue {

    class DamageEntry implements Comparable<DamageEntry> {
        private final Mob entity;
        private int damage;

        public DamageEntry(Mob entity, int damage) {
            this.entity = entity;
            this.damage = damage;
        }

        public void addDamage(int damage) {
            this.damage += damage;
        }

        @Override
        public int compareTo(DamageEntry other) {
            if (other == null || other.getEntity() == null || !other.getEntity().isRegistered()) {
                return -1;
            }
            return Integer.signum(other.damage - damage);
        }

        public int getDamage() {
            return damage;
        }

        public Mob getEntity() {
            return entity;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof DamageEntry) {
                DamageEntry other = (DamageEntry) obj;
                return other.entity.equals(entity) && other.damage == damage;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(entity, damage);
        }

    }

    private final Map<Mob, DamageEntry> attackers = new LinkedHashMap<>();

    public void addDamage(Mob attacker, int damage) {
        final DamageEntry entry = new DamageEntry(attacker, damage);

        if (attackers.containsKey(attacker)) {
            entry.addDamage(attackers.get(attacker).getDamage());
        }

        attackers.put(attacker, entry);
    }

    public Optional<Mob> getHighestDamager() {
        if (attackers.isEmpty()) {
            return Optional.empty();
        }

        DamageEntry pWinner = null;
        DamageEntry mWinner = null;

        for (Mob entity : attackers.keySet()) {

            if (entity == null || !entity.isRegistered()) {
                continue;
            }

            DamageEntry next = attackers.get(entity);

            if (entity.isPlayer()) {
                if (pWinner == null || pWinner.getDamage() < next.getDamage()) {
                    pWinner = next;
                }
            }

            if (entity.isMob()) {
                if (mWinner == null || mWinner.getDamage() < next.getDamage()) {
                    mWinner = next;
                }
            }
        }

        if (pWinner == null && mWinner == null) {
            return Optional.empty();
        }

        return Optional.of(pWinner == null ? mWinner.getEntity() : pWinner.getEntity());
    }

    public void reset() {
        attackers.clear();
    }

}

