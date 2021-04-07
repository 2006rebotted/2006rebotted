package astraeus.game.model.entity.mob.player.skill;

import java.util.concurrent.TimeUnit;

import astraeus.game.model.Graphic;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.combat.type.CombatType;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.sound.Sound;
import astraeus.net.packet.out.PlaySoundPacket;
import astraeus.net.packet.out.ServerMessagePacket;
import astraeus.net.packet.out.SetWidgetStringPacket;
import astraeus.net.packet.out.UpdateSkillPacket;
import astraeus.util.Stopwatch;
import astraeus.util.StringUtils;

public class SkillSet {

	public static final int[] EXP_FOR_LEVEL = { 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584, 1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291, 7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247, 20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529, 50339, 55649, 61512, 67983, 75127, 83014, 91721, 101333, 111945, 123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886, 273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953, 605032, 668051, 737627, 814445, 899257, 992895, 1096278, 1210421, 1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087, 2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295, 5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577, 10692629, 11805606, 13034431 };

	private static byte binarySearch(double experience, int min, int max) {
		final int mid = (min + max) / 2;
		final double value = EXP_FOR_LEVEL[mid];

		if (value > experience) {
			return binarySearch(experience, min, mid - 1);
		} else if (value == experience || EXP_FOR_LEVEL[mid + 1] > experience) {
			return (byte) (mid + 1);
		}

		return binarySearch(experience, mid + 1, max);
	}

	private final Mob entity;

	private Skill[] skills;

	private double expCounter;

	private Stopwatch lock = new Stopwatch();

	private int unlock;

	public SkillSet(Mob entity) {
		this.entity = entity;
		this.skills = new Skill[SkillData.values().length];

		setDefault();
	}

	public void setExpCounter(int expCounter) {
		this.expCounter = expCounter;
	}

	public int getExpCounter() {
		return (int) expCounter;
	}

	public void addExperience(int skillId, double experience) {
		Player player = entity.getPlayer();
		final Skill skill = skills[skillId];

		if (experience <= 0 || skill.getExperience() >= 200_000_000) {
			return;
		}

		if (skill.getExperience() + experience > 200_000_000) {
			experience = 200_000_000 - skill.getExperience();
		}

		@SuppressWarnings("unused")
		double last = skill.getExperience();

		skill.setExperience(skill.getExperience() + experience);
		expCounter += experience;

		//player.send(new SendExperienceCounter(skillId, (int) skill.getExperience() - (int) last));

		if (skill.getMaxLevel() < 99 && skill.getExperience() >= getExperienceForLevel(skill.getMaxLevel() + 1)) {
			int lastLevel = skill.getMaxLevel();
			skill.setMaxLevel(getLevelForExperience(skill.getExperience()));

			if (skill.getLevel() < skill.getMaxLevel()) {
				changeLevel(skill.getSkill(), skill.getMaxLevel() - lastLevel, skill.getMaxLevel());
			}

			final SkillData skillData = SkillData.values()[skillId];

			final String line1 = "Congratulations! You've just advanced " + StringUtils.getAOrAn(skillData.toString()) + " " + skillData + " level!";
			final String line2 = "You have reached level " + skill.getMaxLevel() + "!";

			player.queuePacket(new ServerMessagePacket("You've reached " + StringUtils.getAOrAn(skillData.toString()) + " " + skillData + " level of " + skill.getMaxLevel() + "."));

			player.queuePacket(new SetWidgetStringPacket (line1, skillData.getFirstLine()));
			player.queuePacket(new SetWidgetStringPacket (line2, skillData.getSecondLine()));
			player.getWidgets().openChatBoxWidget(skillData.getChatbox());
			if (player.attr().get(Player.SOUND_KEY)) {
				player.queuePacket(new PlaySoundPacket(Sound.LEVEL_UP));
			}
			player.startGraphic(new Graphic(199));
		}

		refresh(skillId);
	}
	
	public void addCombatExperience(CombatType combatType, int damage) {
		if (damage <= 0) {
			return;
		}

		Player player = entity.getPlayer();

		damage = damage * combatType.getRate();

		switch (combatType) {

		case RANGE:
			switch (player.getCombat().getAttackType()) {
			case RAPID:
			case ACCURATE:
			case AGGRESSIVE:
			case CONTROLLED:
				addExperience(4, damage);
				break;
			case LONGRANGE:
			case DEFENSIVE:
				addExperience(1, damage / 2);
				addExperience(4, damage / 2);
				break;
			}
			break;

		case MELEE:
			switch (player.getCombat().getAttackType()) {
			case ACCURATE:
				addExperience(0, damage);
				break;

			case AGGRESSIVE:
				addExperience(2, damage);
				break;

			case DEFENSIVE:
				addExperience(1, damage);
				break;

			case CONTROLLED:
				addExperience(0, (int) (damage / 3.0));
				addExperience(1, (int) (damage / 3.0));
				addExperience(2, (int) (damage / 3.0));
				break;

			case LONGRANGE:
				addExperience(4, damage);
				break;
			case RAPID:
				addExperience(1, damage / 2);
				addExperience(4, damage / 2);
				break;
			}
		default:
			break;
		}

		addExperience(3, (int) (damage / 3.0));

	}

	public int calculateCombatLevel() {
		final int attack = getMaxLevel(Skill.ATTACK);
		final int defence = getMaxLevel(Skill.DEFENCE);
		final int strength = getMaxLevel(Skill.STRENGTH);
		final int hp = getMaxLevel(Skill.HITPOINTS);
		final int prayer = getMaxLevel(Skill.PRAYER);
		final int ranged = getMaxLevel(Skill.RANGED);
		final int magic = getMaxLevel(Skill.MAGIC);
		int combatLevel = (int) (((defence + hp) + Math.floor(prayer / 2)) * 0.25D) + 1;
		final double meleeMultiplier = (attack + strength) * 0.32500000000000001D;
		final double rangedMultiplier = Math.floor(ranged * 1.5D) * 0.32500000000000001D;
		final double magicMultiplier = Math.floor(magic * 1.5D) * 0.32500000000000001D;

		if (meleeMultiplier >= rangedMultiplier && meleeMultiplier >= magicMultiplier) {
			combatLevel += meleeMultiplier;
		} else if (rangedMultiplier >= meleeMultiplier && rangedMultiplier >= magicMultiplier) {
			combatLevel += rangedMultiplier;
		} else if (magicMultiplier >= meleeMultiplier && magicMultiplier >= rangedMultiplier) {
			combatLevel += magicMultiplier;
		}

		return combatLevel;
	}

	public void calculateLevels() {
		for (int skill = 0; skill < skills.length; skill++) {
			final int level = getLevelForExperience(skills[skill].getExperience());
			skills[skill].setMaxLevel(level);
			refresh(skill);
		}

		entity.getPlayer().setCombatLevel(calculateCombatLevel());
	}

	public void changeLevel(int skillId, int delta) {
		changeLevel(skillId, delta, Integer.MAX_VALUE);
	}

	public void changeLevel(int skillId, int delta, int limit) {

		if (getLevel(skillId) + delta > limit) {
			delta = limit - getLevel(skillId);
		}

		setLevel(skillId, getLevel(skillId) + delta, false);
	}

	public double getExperience(int skillId) {
		return skills[skillId].getExperience();
	}

	public static double getExperienceForLevel(int level) {
		if (level >= 99) {
			return EXP_FOR_LEVEL[98];
		}

		if (level < 1) {
			return 0;
		}

		return EXP_FOR_LEVEL[level - 1];
	}

	public long getTotalExperience() {
		long total = 0;

		for (int index = 0; index < skills.length; index++) {
			double experience = getExperience(index);

			if (experience > 200_000_000) {
				experience = 200_000_000;
			}

			total += experience;
		}
		return total;
	}

	public int getTotalLevel() {
		int total = 0;

		for (int index = 0; index < skills.length; index++) {
			total += getMaxLevel(index);

		}

		System.out.println(total);
		return total;
	}

	public int getLevel(int skillId) {
		return skills[skillId].getLevel();
	}

	public byte getLevelForExperience(double experience) {
		if (experience >= EXP_FOR_LEVEL[98]) {
			return 99;
		}
		return binarySearch(experience, 0, 98);
	}

	public int getMaxLevel(int skillId) {
		return skills[skillId].getMaxLevel();
	}

	public Skill[] getSkills() {
		return skills;
	}

	public void refresh() {
		for (final Skill skill : skills) {
			refresh(skill.getSkill());
		}
	}

	public void refresh(int skillId) {
		final Skill skill = skills[skillId];
		entity.getPlayer().queuePacket(new UpdateSkillPacket (skill.getSkill(), skill.getLevel(), (int) skill.getExperience()));
	}

	public void reloadSkills() {
		for (int skill = 0; skill < skills.length; skill++) {
			setLevel(skill, getLevelForExperience(skills[skill].getExperience()));
		}
	}

	public void setLevel(int skillId, int level) {
		setLevel(skillId, level, true);
	}

	public void setLevel(int skillId, int level, boolean limit) {
		setLevel(skillId, level, limit, true);
	}

	public void setLevel(int skillId, int level, boolean limit, boolean refresh) {
		if (level < 0) {
			level = 0;
		}

		if (limit && level > skills[skillId].getMaxLevel()) {
			level = skills[skillId].getMaxLevel();
		}

		skills[skillId].setLevel(level);

		if (refresh) {
			refresh(skillId);
		}
	}

	public void setMaxLevel(int skillId, int level) {
		setMaxLevel(skillId, level, true);
	}

	public void setMaxLevel(int skillId, int level, boolean refresh) {
		if (level <= 0) {
			level = 1;
		}
		
		skills[skillId].setMaxLevel(level);
		skills[skillId].setExperience(getExperienceForLevel(level));
		setLevel(skillId, level, refresh);
		
		if (level > 99) {
			level = 99;
		}

		if (refresh) {
			refresh(skillId);
		}
	}
	
	public void setDefault() {
		for (int skill = 0; skill < skills.length; skill++) {
			this.skills[skill] = new Skill(skill, skill == Skill.HITPOINTS ? 10 : 1, skill == Skill.HITPOINTS ? 1154 : 0);			
		}
	}

	public void setSkills(Skill[] skills) {
		this.skills = skills;
	}

	public void lock(int ticks) {
		unlock += ticks;
	}

	public boolean locked() {
		return lock.elapsed(TimeUnit.SECONDS) < unlock;
	}
}
