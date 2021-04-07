package rs2.game.model.entity.object;

/**
 * The definition that represents an in-game object.
 * 
 * @author Vult-R
 */
public class GameObjectDefinition {

	/**
	 * The amount of objects present in a #108 old school revision.
	 */
	public static final int GAME_OBJECT_LIMIT = 28598;

	/**
	 * The array of definitions.
	 */
	private static final GameObjectDefinition[] definitions = new GameObjectDefinition[GAME_OBJECT_LIMIT];	

	/**
	 * Gets the definitions.
	 * 
	 * @return definitions The definitions.
	 */
	public static GameObjectDefinition[] getDefinitions() {
		return definitions;
	}

	/**
	 * Looks up a {@link GameObjectDefinition} by an index.
	 * 
	 * @param index
	 *            The index of the object to lookup.
	 * 
	 * @return The object definitions.
	 */
	public static GameObjectDefinition lookup(int index) {
		if (index >= GAME_OBJECT_LIMIT || index < 0) {
			return null;
		}

		return definitions[index];
	}

	/**
	 * The id of this object.
	 */
	private final int id;

	/**
	 * The name of this object.
	 */
	private final String name;

	/**
	 * The description of this object.
	 */
	private final String description;

	/**
	 * The length of this object.
	 */
	private final int length;

	/**
	 * The width of this object.
	 */
	private final int width;
	
	/**
	 * The safe distance.
	 */
	private final int safeInteractingDistance;

	/**
	 * Determines if this object is solid.
	 */
	private final boolean solid;

	/**
	 * Determines if this object is impenetrable.
	 */
	private final boolean impenetrable;

	/**
	 * Determines if this object is interactive.
	 */
	private final boolean interactive;

	/**
	 * Determines if this object is obstructive.
	 */
	private final boolean obstructive;

	/**
	 * The actions for this object.
	 */
	private final String[] options;

	public GameObjectDefinition(int id, String name, String description, int length, int width, boolean solid, boolean impenetrable, boolean interactive, boolean obstructive, String[] options) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.length = length;
		this.width = width;
		this.solid = solid;
		this.impenetrable = impenetrable;
		this.interactive = interactive;
		this.obstructive = obstructive;
		this.options = options;
		this.safeInteractingDistance = (length + width) / 2 >= 3 ? 3 : (length + width) / 2;
	}

	public String getDescription() {
		return description;
	}

	public int getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public String[] getMenuActions() {
		return options;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}
	
	public boolean isImpenetrable() {
		return impenetrable;
	}

	public boolean isInteractive() {
		return interactive;
	}

	public boolean isObstructive() {
		return obstructive;
	}

	public boolean isSolid() {
		return solid;
	}

	public int getSafeInteractingDistance() {
		return safeInteractingDistance;
	}	
	
}
