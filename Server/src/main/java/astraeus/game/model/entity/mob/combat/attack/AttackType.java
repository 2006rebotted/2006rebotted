package astraeus.game.model.entity.mob.combat.attack;

public enum AttackType {

	ACCURATE(3, 0),

	AGGRESSIVE(0, 0),

	DEFENSIVE(0, 3),

	CONTROLLED(1, 1),

	RAPID(0, 0),

	LONGRANGE(0, 0);

	private int accuraceIncrease;

	private int defensiveIncrease;

	private AttackType(int accuracyIncrease, int defensiveIncrease) {
		this.accuraceIncrease = accuracyIncrease;
		this.defensiveIncrease = defensiveIncrease;

	}

	public int getAccuracyIncrease() {
		return accuraceIncrease;
	}

	public int getDefensiveIncrease() {
		return defensiveIncrease;
	}

}