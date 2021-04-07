package astraeus.game.model.entity.mob.combat.attack;

import java.util.function.BiConsumer;

import astraeus.game.model.Animation;
import astraeus.game.model.Graphic;
import astraeus.game.model.Projectile;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.type.CombatType;

public final class AttackBuilder {
	CombatType combatType = CombatType.MELEE;
	int maxHit;
	Graphic startGraphic;
	Graphic startDefenderGraphic;
	Graphic secondStartGraphic;
	Graphic endGraphic;
	Projectile projectile;
	Projectile secondProjectile;
	Animation animation;
	int hitDelay;
	boolean doubleHit;
	boolean fastDoubleHit;
	int delay;
	BiConsumer<Mob, Hit> onHit;

	public AttackBuilder(CombatType combatType) {
		this.combatType = combatType;
		startGraphic = null;
		startDefenderGraphic = null;
		secondStartGraphic = null;
		endGraphic = null;
		projectile = null;
		secondProjectile = null;
		animation = null;
		hitDelay = 0;
		doubleHit = false;
		fastDoubleHit = false;
		delay = 0;
		onHit = (entity, hit) -> { };
	}

	public AttackBuilder(Attack attack) {
		combatType = attack.getCombatType();
		maxHit = attack.getMaxHit();
		startGraphic = attack.getStartGraphic();
		startDefenderGraphic = attack.getStartDefenderGraphic();
		secondStartGraphic = attack.getSecondStartGraphic();
		endGraphic = attack.getEndGraphic();
		projectile = attack.getProjectile();
		secondProjectile = attack.getSecondProjectile();
		animation = attack.getAnimation();
		hitDelay = attack.getHitDelay();
		doubleHit = attack.isDoubleHit();
		fastDoubleHit = attack.isFastDoubleHit();
		delay = attack.getDelay();
		onHit = attack.getOnHit();
	}
	
	public AttackBuilder setMaxHit(int maxHit) {
		this.maxHit = maxHit;
		return this;
	}
	
	public AttackBuilder setStartGraphic(Graphic startGraphic) {
		this.startGraphic = startGraphic;
		return this;
	}
	
	public AttackBuilder setStartDefenderGraphic(Graphic startDefenderGraphic) {
		this.startDefenderGraphic = startDefenderGraphic;
		return this;
	}
	
	public AttackBuilder setSecondStartGraphic(Graphic secondStartGraphic) {
		this.secondStartGraphic = secondStartGraphic;
		return this;
	}

	public AttackBuilder setEndGraphic(Graphic endGraphic) {
		this.endGraphic = endGraphic;
		return this;
	}

	public AttackBuilder setProjectile(Projectile projectile) {
		this.projectile = projectile;
		return this;
	}
	
	public AttackBuilder setSecondProjectile(Projectile secondProjectile) {
		this.secondProjectile = secondProjectile;
		return this;
	}

	public AttackBuilder setAnimation(Animation animation) {
		this.animation = animation;
		return this;
	}

	public AttackBuilder setHitDelay(int hitDelay) {
		this.hitDelay = hitDelay;
		return this;
	}

	public AttackBuilder setDoubleHit(boolean doubleHit) {
		this.doubleHit = doubleHit;
		return this;
	}
	
	public AttackBuilder setFastDoubleHit(boolean fastDoubleHit) {
		this.fastDoubleHit = fastDoubleHit;
		return this;
	}
	
	public AttackBuilder setDelay(int delay) {
		this.delay = delay;
		return this;
	}
	
	public AttackBuilder setOnHit(BiConsumer<Mob, Hit> onHit) {
		this.onHit = onHit;
		return this;
	}

	public Attack build() {
		return new Attack(this);
	}
	
	@Override
	public String toString() {
		return String.format("ATTACK_BUILDER[type=%s, start=%s, secondStart=%s, end=%s, proj=%s, secondProj=%s, animation=%s, hitDelay=%s, isDouble=%s, isFast=%s]", combatType, startGraphic, secondStartGraphic, endGraphic, projectile, secondProjectile, animation, hitDelay, doubleHit, fastDoubleHit);
	}
}

