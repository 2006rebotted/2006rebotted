package com.runescape;

import java.io.File;
import java.math.BigInteger;

/**
 * The main configuration for the Client
 * 
 * @author Seven
 */
public final class Configuration {
	
	private Configuration() {
		
	}

	public static final BigInteger RSA_MODULUS = new BigInteger(
			"94904992129904410061849432720048295856082621425118273522925386720620318960919649616773860564226013741030211135158797393273808089000770687087538386210551037271884505217469135237269866084874090369313013016228010726263597258760029391951907049483204438424117908438852851618778702170822555894057960542749301583313");

	public static final BigInteger RSA_EXPONENT = new BigInteger("65537");

	/**
	 * Sends client-related debug messages to the client output stream
	 */
	public static boolean client_debug = true;

	/**
	 * The address of the server that the client will be connecting to
	 */
	public static String server_address = "localhost";

	public static final String CACHE_DIRECTORY = System.getProperty("user.home") + File.separator + "Cache/";

	/**
	 * The port of the server that the client will be connecting to
	 */
	public static int server_port = 43594;

	public static boolean useJaggrab = false;

	/**
	 * Toggles a security feature called RSA to prevent packet sniffers
	 */
	public static final boolean ENABLE_RSA = true;

	/**
	 * The url that the users will get redirected to after clicking "New User"
	 */
	public static final String REGISTER_ACCOUNT = "www.google.com";

	/**
	 * A string which indicates the Client's name.
	 */
	public static final String CLIENT_NAME = "RuneScape";

	/**
	 * Dumps map region images when new regions are loaded.
	 */
	public static boolean dumpMapRegions = false;

	/**
	 * Displays debug messages on loginscreen and in-game
	 */
	public static boolean clientData = false;

	/**
	 * Enables the use of music played through the client
	 */
	public static boolean enableMusic = true;

	/**
	 * Toggles the ability for a player to see roofs in-game
	 */
	public static boolean enableRoofs = true;

	/**
	 * Used for change worlds button on login screen
	 */
	public static boolean worldSwitch = false;

	/**
	 * Enables extra frames in-between animations to give the animation a smooth
	 * look
	 */
	public static boolean enableTweening = true;

	/**
	 * Used to repack indexes Index 1 = Models Index 2 = Animations Index 3 =
	 * Sounds/Music Index 4 = Maps
	 */
	public static boolean repackIndexOne = false, repackIndexTwo = false, repackIndexThree = false,
			repackIndexFour = false;

	/**
	 * Dump Indexes Index 1 = Models Index 2 = Animations Index 3 = Sounds/Music
	 * Index 4 = Maps
	 */
	public static boolean dumpIndexOne = false, dumpIndexTwo = false, dumpIndexThree = false, dumpIndexFour = false;

	/**
	 * Shows the ids of items, objects, and npcs on right click
	 */
	public static boolean enableIds = false;

	/**
	 * Used to merge all the OS Buddy XP Drops so the counter doesn't get too
	 * big if you are training a lot of different skills
	 */
	public static boolean xp_merge = true;

	/**
	 * Enables fog effects
	 */
	public static boolean enableFog = true;

	/**
	 * newDamage enables or disables fake constitution.
	 */
	public static boolean newDamage = true;

	/**
	 * npcBits can be changed to what your server's bits are set to.
	 */
	public static final int npcBits = 12;

	/**
	 * Enables the use of run energy
	 */
	public static boolean runEnergy = true;

	/**
	 * Displays health above entities heads
	 */
	public static boolean hpAboveHeads = false;

	/**
	 * Displays names above entities
	 */
	public static boolean namesAboveHeads = false;

	/**
	 * Displays OS Buddy orbs on HUD
	 */
	public static boolean enableOrbs = true;

	/**
	 * Enables/Disables Revision 554 hitmarks
	 */
	public static boolean hitmarks554 = false;

	/**
	 * Enables/Disables Revision 554 health bar
	 */
	public static boolean hpBar554 = false;

	/**
	 * Enables the HUD to display 10 X the amount of hitpoints
	 */
	public static boolean tenXHp = false;

}
