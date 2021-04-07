package rs2.game.model.entity.mob.player.skill;

/**
 * The skill-related requirement.
 * 
 * @author SeVen
 */
public class SkillRequirement {

	private final int level;

	private final SkillData skill;

	public SkillRequirement(int level, SkillData skill) {
		this.level = level;
		this.skill = skill;
	}

	public int getLevel() {
		return level;
	}

	public SkillData getSkill() {
		return skill;
	}

}

