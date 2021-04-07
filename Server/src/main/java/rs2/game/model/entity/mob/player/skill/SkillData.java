package rs2.game.model.entity.mob.player.skill;

public enum SkillData {
    ATTACK(0, 6248, 6249, 6247),
    DEFENCE(1, 6254, 6255, 6253),
    STRENGTH(2, 6207, 6208, 6206),
    HITPOINTS(3, 6217, 6218, 6216),
    RANGED(4, 5453, 6114, 4443),
    PRAYER(5, 6243, 6244, 6242),
    MAGIC(6, 6212, 6213, 6211),
    COOKING(7, 6227, 6228, 6226),
    WOODCUTTING(8, 4273, 4274, 4272),
    FLETCHING(9, 6232, 6233, 6231),
    FISHING(10, 6259, 6260, 6258),
    FIREMAKING(11, 4283, 4284, 4282),
    CRAFTING(12, 6264, 6265, 6263),
    SMITHING(13, 6222, 6223, 6221),
    MINING(14, 4417, 4438, 4416),
    HERBLORE(15, 6238, 6239, 6237),
    AGILITY(16, 4278, 4279, 4277),
    THIEVING(17, 4263, 4264, 4261),
    SLAYER(18, 12123, 12124, 12122),
    FARMING(19, 4889, 4890, 4887),
    RUNECRAFTING(20, 4268, 4269, 4267),
    CONSTRUCTION(21, 4268, 4269, 4267),
    HUNTER(22, 4268, 4269, 4267);

    private final int id;
    private final int firstLine;
    private final int secondLine;
    private final int chatbox;

    private SkillData(int id, int firstLine, int secondLine, int chatbox) {
    	this.id = id;
    	this.firstLine = firstLine;
    	this.secondLine = secondLine;
    	this.chatbox = chatbox;
    }

    public final int getChatbox() {
    	return chatbox;
    }

    public final int getFirstLine() {
    	return firstLine;
    }

    public final int getId() {
    	return id;
    }

    public final int getSecondLine() {
    	return secondLine;
    }

    @Override
    public final String toString() {
    	return capitalize(name().toLowerCase().replaceAll("_", " "));
    }
    
	public static String capitalize(final String string) {
		return Character.toUpperCase(string.charAt(0)) + string.substring(1);
	}
    
}
