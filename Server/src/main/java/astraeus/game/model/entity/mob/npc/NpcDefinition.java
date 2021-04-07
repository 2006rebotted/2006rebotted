package astraeus.game.model.entity.mob.npc;

public final class NpcDefinition {

	/**
	 * The maximum amount of mobs present in a #317.
	 */
	public static final int MOB_LIMIT = 2633;

	private static final NpcDefinition[] DEFINITIONS = new NpcDefinition[MOB_LIMIT];

	public static NpcDefinition get(int id) {
		if (id >= MOB_LIMIT || id < 0) {
			return null;
		}
		
		return DEFINITIONS[id];
	}

	public static NpcDefinition[] getDefinitions() {
		return DEFINITIONS;
	}

	private final int id;

	private final String name;

	private final int combatLevel;

	private final int size;

	private final int standAnimation;
	
	private final int walkAnimation;

	private final int turn180Animation;

	private final int turn90CWAnimation;

	private final int turn90CCWAnimation;
	
	private final int attackAnimation;
	
	private final int blockAnimation;
	
	private final int deathAnimation;

	public NpcDefinition(int id, String name, int combatLevel, int size, int standAnimation, int walkAnimation, int turn180Animation, int turn90CWAnimation, int turn90CCWAnimation, int attack, int block, int death) {
		this.id = id;
		this.name = name;
		this.combatLevel = combatLevel;
		this.size = size;
		this.standAnimation = standAnimation;
		this.walkAnimation = walkAnimation;
		this.turn180Animation = turn180Animation;
		this.turn90CWAnimation = turn90CWAnimation;
		this.turn90CCWAnimation = turn90CCWAnimation;
		this.attackAnimation = attack;
		this.blockAnimation = block;
		this.deathAnimation = death;
	}

	/**
	 * @return the combatLevel
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the turn180Animation
	 */
	public int getTurn180Animation() {
		return turn180Animation;
	}

	/**
	 * @return the turn90CCWAnimation
	 */
	public int getTurn90CCWAnimation() {
		return turn90CCWAnimation;
	}

	/**
	 * @return the turn90CWAnimation
	 */
	public int getTurn90CWAnimation() {
		return turn90CWAnimation;
	}

	/**
	 * @return the walkAnimation
	 */
	public int getWalkAnimation() {
		return walkAnimation;
	}

	/**
	 * @return the standAnimation
	 */
	public int getStandAnimation() {
		return standAnimation;
	}
	
	public int getAttackAnimation() {
		return attackAnimation;
	}
	
	public int getBlockAnimation() {
		return blockAnimation;
	}
	
	public int getDeathAnimation() {
		return deathAnimation;
	}

}