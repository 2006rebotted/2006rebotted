package astraeus.game.model.entity.item;

/**
 * Defines an in game item.
 * 
 * @author Vult-R
 */
public final class ItemDefinition {

	public static final int ITEM_LIMIT = 11791;

	//TODO make item definitions immutable
	public static final ItemDefinition[] definitions = new ItemDefinition[ITEM_LIMIT];	

	private final int id;
	
	private final String name;
	
	private final String examine;
	
	private final int lowAlch;
	
	private final int highAlch;
	
	private final int shopValue;
	
	private final boolean noteable;
	
	private final boolean noted;
	
	private final int unnotedId;
	
	private final int notedId;
	
	private final boolean stackable;
	
	private final double weight;

	public ItemDefinition(int id, String name, String examine, int lowAlch, int highAlch, int shopValue, boolean noteable, boolean noted, int unnotedId, int notedId, boolean stackable, double weight) {
		this.id = id;
		this.name = name;
		this.examine = examine;
		this.lowAlch = lowAlch;
		this.highAlch = highAlch;
		this.shopValue = shopValue;
		this.noteable = noteable;
		this.noted = noted;
		this.unnotedId = unnotedId;
		this.notedId = notedId;
		this.stackable = stackable;
		this.weight = weight;
	}

	public static ItemDefinition[] getDefinitions() {
		return definitions;
	}

	public static ItemDefinition lookup(int id) {
		return definitions[id];
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getExamine() {
		return examine;
	}

	public int getLowAlch() {
		return lowAlch;
	}

	public int getHighAlch() {
		return highAlch;
	}

	public int getShopValue() {
		return shopValue;
	}

	public boolean isNoteable() {
		return noteable;
	}

	public boolean isNoted() {
		return noted;
	}

	public int getUnnotedId() {
		return unnotedId;
	}

	public int getNotedId() {
		return notedId;
	}

	public boolean isStackable() {
		return stackable;
	}

	public double getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "ITEM[" + id + "," + name + "]";
	}

}
