package rs2.game.model.entity.mob.combat;

import rs2.game.model.entity.mob.Mob;
import rs2.game.model.entity.mob.combat.attack.Attack;
import rs2.game.model.entity.mob.combat.attack.AttackBuilder;
import rs2.game.model.entity.mob.combat.attack.AttackType;
import rs2.game.model.entity.mob.combat.type.CombatClass;
import rs2.game.model.entity.mob.combat.type.CombatType;
import rs2.util.Stopwatch;

public class CombatFields {
	
	private final Mob mob;
	private Mob defender;
	private Mob attackedBy;	
	private Mob lastAttacked;
	private Mob entity;
	
	private boolean inCombat;
	private boolean firstHit = false;
	
	private final Stopwatch combatDelay = new Stopwatch();
	
	private CombatType combatType;
	private CombatClass combatClass;
	
	private AttackType attackType;
	private AttackStyle attackStyle;
	
	private Attack attack;
	
	public CombatFields(Mob mob) {
		this.mob = mob;
		combatDelay.reset(Stopwatch.currentTime() - 10_000);
	}

	public Mob getMob() {
		return mob;
	}	
	
	public AttackType getAttackType() {
		return attackType;
	}
	
	public Mob getLastAttacked() {
		return lastAttacked;
	}
	
	public void setLastAttacked(Mob lastAttacked) {
		this.lastAttacked = lastAttacked;
	}

	public void setAttackType(AttackType attackType) {
		this.attackType = attackType;
	}

	public AttackStyle getAttackStyle() {
		return attackStyle;
	}

	public void setAttackStyle(AttackStyle attackStyle) {
		this.attackStyle = attackStyle;
	}
	
	public Mob getDefender() {
		return defender;
	}

	public void setDefender(Mob defender) {
		this.defender = defender;
	}
	
	public Mob getAttackedBy() {
		return attackedBy;
	}
	
	public void setAttackedBy(Mob attackedBy) {
		this.attackedBy = attackedBy;
	}

	public Stopwatch getCombatDelay() {
		return combatDelay;
	}
	
	public boolean inCombat() {
		return inCombat;
	}

	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	
	public boolean isFirstHit() {
		return firstHit;
	}

	public void setFirstHit(boolean firstHit) {
		this.firstHit = firstHit;
	}
	
	public Mob getEntity() {
		return entity;
	}
	
	public CombatType getCombatType() {
		return combatType;
	}
	
	public void setCombatType(CombatType combatType) {
		this.combatType = combatType;
	}
	
	public CombatClass getCombatClass() {
		return combatClass;
	}

	public void setCombatClass(CombatClass combatClass) {
		this.combatClass = combatClass;
	}
	
	public Attack getAttack() {
		return attack;
	}

	public void setAttack(AttackBuilder builder) {
		this.attack = new Attack(builder);
	}
	
}
