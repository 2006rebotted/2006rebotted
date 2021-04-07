package astraeus.game.model.entity.mob.player;

import astraeus.game.event.Event;
import astraeus.game.model.*;
import astraeus.game.model.entity.EntityType;
import astraeus.game.model.entity.item.Item;
import astraeus.game.model.entity.mob.Mob;
import astraeus.game.model.entity.mob.Movement;
import astraeus.game.model.entity.mob.combat.Combat;
import astraeus.game.model.entity.mob.combat.def.NpcCombatDefinition;
import astraeus.game.model.entity.mob.combat.dmg.DamageType;
import astraeus.game.model.entity.mob.combat.dmg.Hit;
import astraeus.game.model.entity.mob.combat.type.CombatType;
import astraeus.game.model.entity.mob.combat.type.PlayerCombat;
import astraeus.game.model.entity.mob.combat.weapon.special.Special;
import astraeus.game.model.entity.mob.npc.Npc;
import astraeus.game.model.entity.mob.player.Prayer.PrayerType;
import astraeus.game.model.entity.mob.player.attr.AttributeKey;
import astraeus.game.model.entity.mob.player.collect.Bank;
import astraeus.game.model.entity.mob.player.collect.Equipment;
import astraeus.game.model.entity.mob.player.collect.Inventory;
import astraeus.game.model.entity.mob.player.event.LogoutEvent;
import astraeus.game.model.entity.mob.player.io.PlayerSerializer;
import astraeus.game.model.entity.mob.player.skill.Skill;
import astraeus.game.model.entity.mob.player.skill.impl.MagicSkill;
import astraeus.game.model.entity.mob.player.skill.impl.MagicSkill.TeleportTypes;
import astraeus.game.model.entity.mob.update.UpdateFlag;
import astraeus.game.model.entity.object.GameObject;
import astraeus.game.model.location.Area;
import astraeus.game.model.sound.Volume;
import astraeus.game.model.widget.WidgetSet;
import astraeus.game.model.widget.dialog.Dialogue;
import astraeus.game.model.widget.dialog.DialogueFactory;
import astraeus.game.model.widget.dialog.OptionDialogue;
import astraeus.net.channel.PlayerChannel;
import astraeus.net.packet.Sendable;
import astraeus.net.packet.out.*;
import astraeus.util.LoggerUtils;
import astraeus.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class Player extends Mob {
	
	private final Logger logger = LoggerUtils.getLogger(Player.class);

	/**
	 * The default appearance of a player.
	 */
	public static final Appearance DEFAULT_APPEARANCE = new Appearance(Appearance.Gender.MALE, 0, 10, 18, 26, 33, 36,
			42, 7, 8, 9, 5, 0);

	/**
	 * The default location a player will spawn.
	 */
	public static final Position defaultSpawn = new Position(3086, 3499);	

	/**
	 * The default location a player will spawn if they died.
	 */
	public static final Position defaultRespawn = new Position(3087, 3502);	

	private ChatMessage chatMessage = new ChatMessage();
	private final PlayerRelation playerRelation = new PlayerRelation(this);
	private final Inventory inventory = new Inventory(this);
	private final Equipment equipment = new Equipment(this);
	private final Bank bank = new Bank(this);
	private final WidgetSet widgets = new WidgetSet(this);
	private final Prayer prayer = new Prayer(this);
	private DialogueFactory dialogueFactory = new DialogueFactory(this);
	private Optional<Dialogue> dialogue = Optional.empty();
	private Optional<OptionDialogue> optionDialogue;
	
	private final PlayerCombat playerCombat = new PlayerCombat(this);	
	private final Special special = new Special(this);	
	private MagicSkill magic = new MagicSkill(this);
	
	private boolean insertItem;

	public int lastMessage = 1;

	private int headIcon = -1;
	private int combatLevel;
	private Appearance appearance = Player.DEFAULT_APPEARANCE;
	private PlayerRights rights = PlayerRights.PLAYER;

	private PlayerChannel session;

	private String username;
	private String password;
	private String uuid;

	private int runEnergy = 100;
	private int runRestore;

	private int specialAmount = 100;

	private int wildernessLevel;
	private boolean inWilderness;
	private boolean inMultiCombat;
	
	private boolean withdrawAsNote;

	public static final AttributeKey<Boolean> ACCEPT_AID_KEY = AttributeKey.valueOf("accept_aid", true);
	public static final AttributeKey<Boolean> ACTIVE_KEY = AttributeKey.valueOf("active", false);
	public static final AttributeKey<Boolean> AUTO_RETALIATE_KEY = AttributeKey.valueOf("auto_retaliate", true);
	public static final AttributeKey<Boolean> BANKING_KEY = AttributeKey.valueOf("banking", false);
	public static final AttributeKey<Boolean> CHAT_EFFECTS_KEY = AttributeKey.valueOf("chat_effects", true);
	public static final AttributeKey<Boolean> DEBUG_KEY = AttributeKey.valueOf("debug", false);
	public static final AttributeKey<Boolean> DEBUG_NETWORK_KEY = AttributeKey.valueOf("debug_network", false);
	public static final AttributeKey<Boolean> DISCONNECTED_KEY = AttributeKey.valueOf("disconnected", false);
	public static final AttributeKey<Boolean> LOGOUT_KEY = AttributeKey.valueOf("logout", false);
	public static final AttributeKey<Boolean> MUSIC_KEY = AttributeKey.valueOf("music", true);
	public static final AttributeKey<Boolean> NEW_PLAYER_KEY = AttributeKey.valueOf("new_player", false);
	public static final AttributeKey<Boolean> MOUSE_BUTTON_KEY = AttributeKey.valueOf("mouse_button", false);
	public static final AttributeKey<Boolean> SAVE_KEY = AttributeKey.valueOf("save", false);
	public static final AttributeKey<Boolean> SHOPPING_KEY = AttributeKey.valueOf("shopping", false);
	public static final AttributeKey<Boolean> SOUND_KEY = AttributeKey.valueOf("sound", true);
	public static final AttributeKey<Boolean> SPLIT_CHAT_KEY = AttributeKey.valueOf("split_chat_key", true);
	public static final AttributeKey<Boolean> CHANGING_APPEARANCE_KEY = AttributeKey.valueOf("changing_appearance_key", false);

	public static final AttributeKey<Volume> AREA_SOUND_VOLUME_KEY = AttributeKey.valueOf("area_sound_volume",
			Volume.NORMAL);
	public static final AttributeKey<Volume> MUSIC_VOLUME_KEY = AttributeKey.valueOf("music_volume", Volume.NORMAL);
	public static final AttributeKey<Volume> SOUND_EFFECT_VOLUME_KEY = AttributeKey.valueOf("sound_effect_volume",
			Volume.NORMAL);

	public static final AttributeKey<Brightness> BRIGHTNESS_KEY = AttributeKey.valueOf("brightness", Brightness.NORMAL);

	// actual player
	public Player(final PlayerChannel session) {
		super(Player.defaultSpawn);
		this.session = session;
	}

	// bots
	public Player(String username, Position location) {
		super(location);
		this.username = username;
	}

	public void createAttributes() {
		attr.put(ACCEPT_AID_KEY, true);
		attr.put(ACTIVE_KEY, false);
		attr.put(AREA_SOUND_VOLUME_KEY, Volume.NORMAL);
		attr.put(AUTO_RETALIATE_KEY, true);
		attr.put(BANKING_KEY, false);
		attr.put(BRIGHTNESS_KEY, Brightness.NORMAL);
		attr.put(CHAT_EFFECTS_KEY, true);
		attr.put(DEBUG_KEY, false);
		attr.put(DEBUG_NETWORK_KEY, false);
		attr.put(DISCONNECTED_KEY, false);
		attr.put(LOGOUT_KEY, false);
		attr.put(SAVE_KEY, false);
		attr.put(SHOPPING_KEY, false);
		attr.put(SOUND_KEY, true);
		attr.put(SOUND_EFFECT_VOLUME_KEY, Volume.NORMAL);
		attr.put(SPLIT_CHAT_KEY, true);
		attr.put(MOUSE_BUTTON_KEY, false);
		attr.put(MUSIC_KEY, true);
		attr.put(MUSIC_VOLUME_KEY, Volume.NORMAL);
		attr.put(NEW_PLAYER_KEY, false);
		attr.put(Movement.RUNNING_KEY, true);
		attr.put(Movement.LOCK_MOVEMENT, false);
		attr.put(Combat.TELE_BLOCK_KEY, false);
	}

	@Override
	public void onTick() {
		handleRunRestore();

		prayer.drain();
	}

	@Override
	public boolean canLogout() {
		return true;
	}
	
	public void logout() {
		if (!canLogout()) {
			return;
		}
		
		post(new LogoutEvent(this));
	}
	
	public void onDeregister() {
		save();		
		queuePacket(new LogoutPlayerPacket());		
		session.getChannel().close();
		World.world.deregister(this);
		logger.info(String.format("[DEREGISTERED]: [host= %s]", session.getHostAddress()));
	}
	
	public void save() {
		PlayerSerializer.encode(this);
	}

	@Override
	public boolean canTeleport() {
		return true;
	}

	@Override
	public boolean canClickButton() {
		return true;
	}

	@Override
	public boolean canTrade() {
		return true;
	}

	@Override
	public boolean canDuel() {
		return false;
	}

	@Override
	public boolean canAttackMob(Npc mob) {
		return true;
	}

	@Override
	public boolean canAttackPlayer(Player player) {
		return false;
	}

	@Override
	public boolean canClickMob(Npc mob) {
		return true;
	}

	@Override
	public boolean canClickObject(GameObject object) {
		return true;
	}

	@Override
	public boolean canDrink() {
		return true;
	}

	@Override
	public boolean canEat() {
		return true;
	}

	@Override
	public boolean canDrop() {
		return true;
	}

	@Override
	public boolean canMove() {
		return true;
	}

	@Override
	public boolean canPickup(Item item) {
		return true;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean canPray() {
		return true;
	}

	@Override
	public boolean canTalk() {
		return true;
	}

	@Override
	public boolean canUnequip(Item item) {
		return true;
	}

	@Override
	public boolean canUseSpecial() {
		return true;
	}

	@Override
	public void onDeath() {

	}

	@Override
	public void onMovement() {
		displayWalkableInterfaces();

		handleRunEnergy();
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public void preUpdate() {
		// first handle the packets
		session.handleQueuedPackets();

		// second movement
		movement.handleEntityMovement();

		// lastly anything else before the npc is updated
		onTick();
	}
	
	public void update() {
		synchronized (this) {
			flushPacket(new UpdatePlayerPacket());
			flushPacket(new UpdateNpcPacket());
		}
	}

	@Override
	public void postUpdate() {
		getUpdateFlags().clear();
		getAnimations().clear();
		getGraphics().clear();
		setRegionChange(false);
		setTeleporting(false);
	}

	@Override
	public void decrementHealth(int damage) {

	}

	@Override
	public int getCurrentHealth() {
		return getSkills().getLevel(Skill.HITPOINTS);
	}

	@Override
	public int getMaximumHealth() {
		return getSkills().getMaxLevel(Skill.HITPOINTS);
	}

	public void displayWalkableInterfaces() {
		if (Area.inWilderness(this)) {
			int calculateY = this.getPosition().getY() > 6400 ? super.getPosition().getY() - 6400
					: super.getPosition().getY();
			wildernessLevel = (((calculateY - 3520) / 8) + 1);
			if (!inWilderness) {
				queuePacket(new DisplayWalkableWidgetPacket(197));
				queuePacket(new SetPlayerOptionPacket(PlayerOption.ATTACK));
				inWilderness = true;
			}
			queuePacket(new SetWidgetStringPacket("@yel@Level: " + wildernessLevel, 199));
		} else if (inWilderness) {
			queuePacket(new SetPlayerOptionPacket(PlayerOption.ATTACK, true));
			queuePacket(new DisplayWalkableWidgetPacket(-1));
			inWilderness = false;
			wildernessLevel = 0;
		}
		if (Area.inMultiCombat(this)) {
			if (!inMultiCombat) {
				queuePacket(new DisplayMultiIconPacket(false));
				inMultiCombat = true;
			}
		} else {
			queuePacket(new DisplayMultiIconPacket(true));
			inMultiCombat = false;
		}
	}

	/**
	 * Moves a {@code player} to a target {@code location.
	 *
	 * @param player
	 *            The player that is being moved.
	 *
	 * @param oosition
	 *            The location the player will be sent to.
	 */
	public void move(Position oosition) {		
		setLastPosition(position.copy());
		setPosition(new Position(oosition.copy()));
		setTeleporting(true);
		getMovement().reset();
		getUpdateFlags().add(UpdateFlag.APPEARANCE);
	}

	private void handleRunRestore() {
		if (attr.get(Movement.RUNNING_KEY) && movement.isMoving()) {
			return;
		}

		setRunRestore(getRunRestore() + 1);

		if (getRunRestore() == 3) {
			setRunRestore(0);

			int energy = getRunEnergy() + 1;

			if (energy > 100) {
				energy = 100;
			}

			setRunEnergy(energy);
			queuePacket(new SetRunEnergyPacket());
		}
	}

	private void handleRunEnergy() {
		if (!attr.get(Movement.RUNNING_KEY)) {
			return;
		}

		if (getRunEnergy() <= 0) {
			attr.put(Movement.RUNNING_KEY, false);
			queuePacket(new SetWidgetConfigPacket(152, 0));
		} else {
			setRunEnergy(getRunEnergy() - 1);
			queuePacket(new SetRunEnergyPacket());
		}
	}	

	/**
	 * Posts an event to this world's event provider.
	 *
	 * @param event
	 *            The event to post.
	 */
	public <E extends Event> void post(E event) {
		World.world.post(this, event);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getPassword());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Player) {
			Player other = (Player) obj;
			return other.hashCode() == hashCode();
		}

		return false;
	}
	
	public MagicSkill getMagic() {
		return magic;
	}

	public void setMagic(MagicSkill magic) {
		this.magic = magic;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getHostAddress() {
		return session.getHostAddress();
	}

	public long getLongUsername() {
		return StringUtils.stringToLong(username);
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public ChatMessage getChatMessage() {
		return chatMessage;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public PlayerRelation getRelation() {
		return playerRelation;
	}

	public PlayerRights getRights() {
		return rights;
	}

	public PlayerChannel getSession() {
		return session;
	}

	/**
	 * Queues a packet to be processed in sequence.
	 * 
	 * @param out
	 * 		The packet to queue.
	 */
	public void queuePacket(final Sendable out) {
		this.session.queue(out);
	}

	/**
	 * Sends a packet to the client immediately.
	 * 
	 * @note This should never be used only by player update and npc update packet.
	 * All other packets can be queued.
	 * 
	 * @param out
	 * 		The packet to send.
	 */
	private void flushPacket(final Sendable out) {
		this.session.flush(out);
	}
	
	public Special getSpecial() {
		return special;
	}

	public void setAppearance(Appearance appearance) {
		this.appearance = appearance;
		getUpdateFlags().add(UpdateFlag.APPEARANCE);
	}

	public int getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(int headIcon) {
		this.headIcon = headIcon;
		getUpdateFlags().add(UpdateFlag.APPEARANCE);
	}

	public void setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
	}

	public void setRights(final PlayerRights rights) {
		this.rights = rights;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Bank getBank() {
		return bank;
	}

	public int getRunEnergy() {
		return runEnergy;
	}

	public void setRunEnergy(int runEnergy) {
		this.runEnergy = runEnergy;
	}

	public int getRunRestore() {
		return runRestore;
	}

	public void setRunRestore(int runRestore) {
		this.runRestore = runRestore;
	}

	public int getSpecialAmount() {
		return specialAmount;
	}

	public void setSpecialAmount(int specialAmount) {
		this.specialAmount = specialAmount;
	}

	public String getUUID() {
		return uuid;
	}

	public WidgetSet getWidgets() {
		return widgets;
	}
	
	public DialogueFactory getDialogueFactory() {
		return dialogueFactory;
	}

	public void setDialogueFactory(DialogueFactory dialogueFactory) {
		this.dialogueFactory = dialogueFactory;
	}

	public Optional<OptionDialogue> getOptionDialogue() {
		return optionDialogue;
	}

	public void setOptionDialogue(Optional<OptionDialogue> optionDialogue) {
		this.optionDialogue = optionDialogue;
	}
	
	public Optional<Dialogue> getDialogue() {
		return dialogue;
	}
	
	public void setDialogue(final Optional<Dialogue> dialogue) {
		this.dialogue = dialogue;
	}	

	public boolean isWithdrawAsNote() {
		return withdrawAsNote;
	}

	public void setWithdrawAsNote(boolean withdrawAsNote) {
		this.withdrawAsNote = withdrawAsNote;
	}
	
    /**
     * Determines if items should be inserted when banking.
     *
     * @return {@code true} if items should be inserted, {@code false}
     *         otherwise.
     */
    public boolean isInsertItem() {
        return insertItem;
    }

    /**
     * Sets the value for {@link Player#insertItem}.
     *
     * @param insertItem
     *            the new value to set.
     */
    public void setInsertItem(boolean insertItem) {
        this.insertItem = insertItem;
    }    
    
    public void setCombatLevel(int combatLevel) {
    	this.combatLevel = combatLevel;
    }

	public Prayer getPrayer() {
		return prayer;
	}

	@Override
	public int getHashCode() {
		return Objects.hash(username, password);
	}

	@Override
	public EntityType type() {
		return EntityType.PLAYER;
	}

	@Override
	public void hit(Mob attacker, Hit hit) {
		int damage = hit.getDamage();
		
		if (getEquipment().hasShield()) {
			if (getEquipment().get(Equipment.SHIELD_SLOT).getId() == 12817) {
				if (Math.random() > 0.3) {
					hit.setDamage((int) (hit.getDamage() * 0.75));
					startGraphic(new Graphic(Priority.EXTRA_HIGH, 321));
				}
			}
		}

//		addHit(hit);
//		
//		if (damage > 0) {
//			getCombat().getDamageQueue().addDamage(attacker == null ? this : attacker, damage);
//		}

		decrementHealth(damage);

		if (!widgets.isEmpty()) {
			widgets.close();
		}

//		if (getCurrentHealth() <= 0) {
//			TaskQueue.queue(new EntityDeathTask(getCombat().getDamageQueue().getHighestDamager().orElse(attacker == null ? this : attacker), this));
//		}
	}

	@Override
	public void onDamage(Mob attacker, Hit hit) {
		if (getPrayer().active(PrayerType.REDEMPTION)) {
			int level = getSkills().getLevel(Skill.HITPOINTS) - hit.getDamage();
			int max = getSkills().getMaxLevel(Skill.HITPOINTS);

			if (level > 0 && level <= max * 0.10) {
				int prayer = getSkills().getLevel(Skill.PRAYER);

				getSkills().changeLevel(Skill.HITPOINTS, (int) (prayer * 0.25));
				getSkills().setLevel(Skill.PRAYER, 0);
				startGraphic(new Graphic(436));
			}
		}

		if (getEquipment().contains(11090)) {
			int level = getSkills().getLevel(Skill.HITPOINTS) - hit.getDamage();
			int max = getSkills().getMaxLevel(Skill.HITPOINTS);

			if (level > 0 && level <= max * 0.20) {
				queuePacket(new ServerMessagePacket("The Phoenix necklace of life saves you but was destroyed in the process"));
				getEquipment().remove(new Item(11090));
				getSkills().changeLevel(Skill.HITPOINTS, (int) (max * 0.30));
			}
		}

		if (getEquipment().contains(2570)) {
			int level = getSkills().getLevel(Skill.HITPOINTS) - hit.getDamage();
			int max = getSkills().getMaxLevel(Skill.HITPOINTS);

			if (level > 0 && level <= max * 0.10) {
				getMagic().teleport(new Position(3160, 3485), TeleportTypes.SPELL_BOOK, false);
				getEquipment().remove(new Item(2570));
				attacker.getCombat().reset();
				queuePacket(new ServerMessagePacket("The Ring of life has saved you; but was destroyed in the process."));
			}
		}

		if (getEquipment().get(Equipment.RING_SLOT) != null) {
			Item ring = getEquipment().get(Equipment.RING_SLOT);
			if (ring.getId() == 2550 && hit != null) {

				if (hit.getDamage() > 0) {

					int delt = (int) Math.ceil(hit.getDamage() * 0.10);

					if (attacker != null) {
						attacker.hit(this, new Hit(delt, DamageType.NONE));
					}

					playerCombat.setRecoil(playerCombat.getRecoil() + 1);

					if (playerCombat.getRecoil() >= 40) {
						playerCombat.setRecoil(0);
						getEquipment().remove(new Item(2550));
						queuePacket(new ServerMessagePacket("<col=9A289E>Your ring of recoil has shattered!"));
					}
				}
			}
		}
		
		if (getSkills().getLevel(Skill.HITPOINTS) <= 0 && attacker != null && !attacker.isDead() && !attacker.equals(this) && getPrayer().active(PrayerType.RETRIBUTION)) {
			startGraphic(new Graphic(437));

			attacker.hit(this, new Hit((int) (0.25 * getSkills().getMaxLevel(Skill.PRAYER))));

			if (Area.inMultiCombat(this)) {
				for (Player player : getLocalPlayers()) {
					if (player != null && !player.equals(this) && !player.equals(attacker) && player.isRegistered()) {
						if (!Area.inMultiCombat(player)) {
							continue;
						}

						if (Position.getManhattanDistance(getPosition(), player.getPosition()) <= 3) {
							player.hit(this, new Hit((int) (0.25 * getSkills().getLevel(Skill.PRAYER))));
						}
					}
				}

				for (Mob mob : getLocalNpcs()) {
					if (mob != null && !mob.equals(this) && mob.isRegistered() && NpcCombatDefinition.get(mob.getId()) != null) {
						if (Position.getManhattanDistance(this.getPosition(), mob.getPosition()) <= 3) {
							mob.hit(this, new Hit((int) (0.25 * getSkills().getLevel(Skill.PRAYER))));
						}
					}
				}
			}
		}

		if (getCurrentHealth() <= 0) {
			//TaskQueue.queue(new EntityDeathTask(getCombat().getDamageQueue().getHighestDamager().orElse(attacker == null ? this : attacker), this));
		}		
	}

	@Override
	public boolean canAttack(Mob defender, CombatType type) {
		// TODO
		return false;
	}

	@Override
	public void buildAttack(CombatType type) {
		// TODO
	}

}
