package astraeus.game.model.entity.mob.combat.attack;

import java.util.function.BiConsumer;

import astraeus.game.model.Animation;
import astraeus.game.model.Graphic;
import astraeus.game.model.Projectile;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.type.CombatType;

public final class Attack {
	private final CombatType combatType;
	private final Graphic startGraphic;
	private final Graphic startDefenderGraphic;
	private final Graphic secondStartGraphic;
	private final Graphic endGraphic;
	private final Projectile projectile;
	private final Projectile secondProjectile;
	private final Animation animation;
	private final int hitDelay;
	private final int maxHit;
	private final boolean doubleHit;
	private final boolean fastDoubleHit;
	private final int delay;
	private final BiConsumer<Mob, Hit> onHit;
	
	private Attack(Attack attack) {
		combatType = attack.combatType;
		maxHit = attack.maxHit;
		startGraphic = attack.startGraphic;
		startDefenderGraphic = attack.startDefenderGraphic;
		secondStartGraphic = attack.secondStartGraphic;
		endGraphic = attack.endGraphic;
		projectile = attack.projectile;
		secondProjectile = attack.secondProjectile;
		animation = attack.animation;
		hitDelay = attack.hitDelay;
		doubleHit = attack.doubleHit;
		fastDoubleHit = attack.fastDoubleHit;
		delay = attack.getDelay();
		onHit = attack.getOnHit();
	}

	public Attack(AttackBuilder builder) {
		combatType = builder.combatType;
		maxHit = builder.maxHit;
		startGraphic = builder.startGraphic;
		startDefenderGraphic = builder.startDefenderGraphic;
		secondStartGraphic = builder.secondStartGraphic;
		endGraphic = builder.endGraphic;
		projectile = builder.projectile;
		secondProjectile = builder.secondProjectile;
		animation = builder.animation;
		hitDelay = builder.hitDelay;
		doubleHit = builder.doubleHit;
		fastDoubleHit = builder.fastDoubleHit;
		delay = builder.delay;
		onHit = builder.onHit;
	}

	public CombatType getCombatType() {
		return combatType;
	}
	
	public int getMaxHit() {
		return maxHit;
	}

	public Graphic getStartGraphic() {
		return startGraphic;
	}
	
	public Graphic getStartDefenderGraphic() {
		return startDefenderGraphic;
	}
	
	public Graphic getSecondStartGraphic() {
		return secondStartGraphic;
	}

	public Graphic getEndGraphic() {
		return endGraphic;
	}

	public Projectile getProjectile() {
		return projectile;
	}

	public Projectile getSecondProjectile() {
		return secondProjectile;
	}

	public Animation getAnimation() {
		return animation;
	}

	public int getHitDelay() {
		return hitDelay;
	}
	
	public boolean isDoubleHit() {
		return doubleHit;
	}
	
	public boolean isFastDoubleHit() {
		return fastDoubleHit;
	}
	
	public int getDelay() {
		return delay;
	}
	
	public BiConsumer<Mob, Hit> getOnHit() {
		return onHit;
	}
	
	public Attack copy() {
		return new Attack(this);
	}
	
	@Override
	public String toString() {
		return String.format("ATTACK[type=%s, start=%s, secondStart=%s, end=%s, proj=%s, secondProj=%s, animation=%s, hitDelay=%s, isDouble=%s, isFast=%s]", combatType, startGraphic, secondStartGraphic, endGraphic, projectile, secondProjectile, animation, hitDelay, doubleHit, fastDoubleHit);
	}
}
