package rs2.game.model.entity.mob.combat.type;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.dmg.Hit;

public interface EntityCombat {
	
	public boolean canAttack(Mob attacker, Mob defender, CombatType type);

	public void onAttack(Mob attacker, Mob defender, Hit hit, CombatType type);

	public void onHit(Mob attacker, Mob defender, Hit hit, CombatType type);
	
	public int getCombatDelay(Mob attacker);

	public void setCombatAnimations(Mob attacker);

	public void setAttack(Mob attacker);

	public void buildAttack(Mob attacker, CombatType type);
	
	public void onDamage(Mob attacker, Mob defender, Hit hit);
	
	public boolean canTakeDamage(Mob defender);
	
}
