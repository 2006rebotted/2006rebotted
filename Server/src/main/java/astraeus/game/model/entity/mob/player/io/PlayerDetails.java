package astraeus.game.model.entity.mob.player.io;

import astraeus.game.model.Brightness;
import astraeus.game.model.Position;
import astraeus.game.model.entity.mob.Movement;
import astraeus.game.model.entity.mob.player.Appearance;
import astraeus.game.model.entity.mob.player.Player;
import astraeus.game.model.entity.mob.player.PlayerRights;
import astraeus.game.model.entity.mob.player.skill.Skill;
import astraeus.game.model.sound.Volume;

import java.io.*;
import java.util.List;

/**
 * The class used to create an object and convert to json object.
 *
 * @author Seven
 */
public final class PlayerDetails {

    /**
     * Determines if a {@code player} can be deserialized.
     *
     * @param player The player to check.
     *
     * @return {@code true} If a player can be deserialized, {@code false} otherwise.
     */
    public static boolean deserialize(Player player) throws Exception {
        BufferedReader reader = null;
        try {
            final File file = new File(
                    "./Data/characters/details/" + player.getUsername() + ".json");

            if (!file.exists()) {
                return false;
            }

            reader = new BufferedReader(new FileReader(file));

            final PlayerDetails details =
                    PlayerSerializer.GSON.fromJson(reader, PlayerDetails.class);
            player.setUsername(details.username);
            player.setPassword(details.password);
            player.setRights(details.rights);
            player.setPosition(player.attr().get(Player.NEW_PLAYER_KEY) ? Player.defaultSpawn
                    : details.location);
            player.attr().put(Player.NEW_PLAYER_KEY, details.newPlayer);
            player.attr().put(Player.BRIGHTNESS_KEY, details.brightness);
            player.attr().put(Player.MUSIC_VOLUME_KEY, details.musicVolume);
            player.attr().put(Player.SOUND_EFFECT_VOLUME_KEY, details.soundEffectVolume);
            player.attr().put(Player.AREA_SOUND_VOLUME_KEY, details.areaSoundVolume);
            player.attr().put(Movement.RUNNING_KEY, details.running);
            player.attr().put(Player.AUTO_RETALIATE_KEY, details.autoRetaliate);
            player.attr().put(Player.SOUND_KEY, details.enableSound);
            player.attr().put(Player.MUSIC_KEY, details.enableMusic);
            player.attr().put(Player.DEBUG_KEY, details.debugMode);
            player.attr().put(Player.MOUSE_BUTTON_KEY, details.mouseButtons);
            player.attr().put(Player.CHAT_EFFECTS_KEY, details.chatEffects);
            player.attr().put(Player.SPLIT_CHAT_KEY, details.splitChat);
            player.attr().put(Player.ACCEPT_AID_KEY, details.acceptAid);

            if (details.appearance == null) {
                player.getAppearance().getDefaultAppearance();
            } else {
                player.setAppearance(details.appearance);
            }
            
			if (details.skills != null) {
				player.getSkills().setSkills(details.skills);
			} else {
                player.getSkills().setDefault();
            }

            if (details.friendList.size() > 0) {
                player.getRelation().setFriendList(details.friendList);
            }
            if (details.ignoreList.size() > 0) {
                player.getRelation().setIgnoreList(details.ignoreList);
            }
            return true;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private final String username;
    private final String password;
    @SuppressWarnings("unused")
	private final String hostAddress;
    private final PlayerRights rights;
    private final Position location;
    private final Brightness brightness;
    private final Volume musicVolume;
    private final Volume soundEffectVolume;
    private final Volume areaSoundVolume;
    private final boolean newPlayer;
    private final boolean running;
    private final boolean autoRetaliate;
    private final boolean enableSound;
    private final boolean enableMusic;
    private final boolean debugMode;
    private final boolean mouseButtons;
    private final boolean chatEffects;
    private final boolean splitChat;
    private final boolean acceptAid;
    private final Appearance appearance;
    private final Skill[] skills;
    private final List<Long> friendList;
    private final List<Long> ignoreList;

    /**
     * Creates a new {@link PlayerDetails}.
     *
     * @param player The player to serialize.
     *
     *        Note any of the attributes placed in the constructor will be serialized.
     */
    public PlayerDetails(Player player) {
        username = player.getUsername();
        password = player.getPassword();
        hostAddress = player.getHostAddress();
        rights = player.getRights();
        location = player.getPosition();
        brightness = player.attr().get(Player.BRIGHTNESS_KEY);
        musicVolume = player.attr().get(Player.MUSIC_VOLUME_KEY);
        soundEffectVolume = player.attr().get(Player.SOUND_EFFECT_VOLUME_KEY);
        areaSoundVolume = player.attr().get(Player.AREA_SOUND_VOLUME_KEY);
        newPlayer = player.attr().get(Player.NEW_PLAYER_KEY);
        running = player.attr().get(Movement.RUNNING_KEY);
        autoRetaliate = player.attr().get(Player.AUTO_RETALIATE_KEY);
        enableSound = player.attr().get(Player.SOUND_KEY);
        enableMusic = player.attr().get(Player.MUSIC_KEY);
        debugMode = player.attr().get(Player.DEBUG_KEY);
        mouseButtons = player.attr().get(Player.MOUSE_BUTTON_KEY);
        chatEffects = player.attr().get(Player.CHAT_EFFECTS_KEY);
        splitChat = player.attr().get(Player.SPLIT_CHAT_KEY);
        acceptAid = player.attr().get(Player.ACCEPT_AID_KEY);
        appearance = player.getAppearance();
        skills = player.getSkills().getSkills();
        friendList = player.getRelation().getFriendList();
        ignoreList = player.getRelation().getIgnoreList();
    }

    public String getUsername() {
        return username;
    }

    public PlayerRights getRights() {
        return rights;
    }

    public Position getLocation() {
        return location;
    }

    public Appearance getAppearance() {
        return appearance;
    }
    
    public Skill[] getSkills() {
    	return skills;
    }

    /**
     * Serializes the converted object into json.
     */
    public void serialize() throws Exception {
        BufferedWriter writer = null;
        
        final File dir = new File("./Data/characters/details/");
        
        if (!dir.exists()) {
        	dir.mkdirs();
        }        
        
        try {
            writer = new BufferedWriter(new FileWriter(
                    dir.toString() + File.separator + username + ".json", false));
            writer.write(PlayerSerializer.GSON.toJson(this));
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
