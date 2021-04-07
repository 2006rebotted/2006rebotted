package rs2.game.model.entity.mob.combat.def;

import java.util.HashMap;
import java.util.Map;

import rs2.game.model.entity.mob.combat.type.CombatType;
import rs2.game.model.entity.mob.player.skill.Skill;

public final class NpcCombatDefinition {

	private static final Map<Integer, NpcCombatDefinition> definitions = new HashMap<>();
	
	private final String name;
	private final int id;
	private final CombatType combatType;
	private final Skill[] skills;
	private final int[] bonuses;
	private final int magicMaxHit;
	private final int attackSpeed;
	private final int respawnTime;

	public NpcCombatDefinition(String name, int id, CombatType combatType, Skill[] skills, int[] bonuses, int magicMaxHit, int attackSpeed, int respawnTime) {
		this.name = name;
		this.id = id;
		this.combatType = combatType;
		this.skills = skills;
		this.bonuses = bonuses;
		this.attackSpeed = attackSpeed;
		this.magicMaxHit = magicMaxHit;
		this.respawnTime = respawnTime;
	}
	
	public static NpcCombatDefinition get(int id) {
		return definitions.get(id);
	}

	public static Map<Integer, NpcCombatDefinition> getMobCombatDefinition() {
		return definitions;
	}

	public int getAttackSpeed() {
		return attackSpeed;
	}

	public CombatType getCombatType() {
		return combatType;
	}

	public int getHealth() {
		return skills[3].getLevel();
	}
	
	public Skill[] getSkills() {
		return skills;
	}
	
	public int getMagicMaxHit() {
		return magicMaxHit;
	}
	
	public int[] getBonuses() {
		return bonuses;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRespawnTime() {
		return respawnTime;
	}

}
