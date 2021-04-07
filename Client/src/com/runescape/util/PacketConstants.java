package com.runescape.util;
/**
 * The class that contains packet-related constants.
 * 
 * @author Seven
 * @author TheChosenOne
 */
public final class PacketConstants {
	
	private PacketConstants() {
		
	}
	
	public static final int FOCUS_CHANGE = 3;
	
	public static final int FLAG_ACCOUNT = 45;
	
	public static final int ADD_FRIEND = 188;
	
	public static final int REPORT_PLAYER = 218;
	
	public static final int IDLE = 0;
	
	public static final int CAMERA_MOVEMENT = 86;
	
	public static final int ENTER_REGION = 210;

	public static final int PLAYER_UPDATING = 81;

	public static final int DELETE_GROUND_ITEM = 64;

	public static final int SEND_REMOVE_GROUND_ITEM = 156;

	public static final int SEND_OBJECT = 151;

	public static final int TRANSFORM_PLAYER_TO_OBJECT = 147;

	public static final int SEND_REMOVE_OBJECT = 101;
	
	public static final int DESIGN_SCREEN = 101;

	public static final int SEND_PROJECTILE = 117;

	public static final int ANIMATE_OBJECT = 160;

	public static final int SEND_ALTER_GROUND_ITEM_COUNT = 84;

	public static final int SEND_GROUND_ITEM = 44;

	public static final int SEND_GFX = 4;

	public static final int OPEN_WELCOME_SCREEN = 176;

	public static final int SHOW_PLAYER_HEAD_ON_INTERFACE = 185;
	
	public static final int BUTTON_CLICK = 185;

	public static final int CLAN_CHAT = 217; // 317 did not have this

	public static final int RESET_CAMERA = 107;

	public static final int CLEAN_ITEMS_OF_INTERFACE = 72;

	public static final int SHOW_IGNORE_NAMES = 214;
	
	public static final int MOVE_ITEM = 214;

	public static final int SPIN_CAMERA = 166;

	public static final int SEND_SKILL = 134;

	public static final int SEND_SIDE_TAB = 71;

	public static final int PLAY_SONG = 74;

	public static final int NEXT_OR_PREVIOUS_SONG = 121;
	
	public static final int LOADED_REGION = 121;

	public static final int LOGOUT = 109;

	public static final int MOVE_COMPONENT = 70;

    public static final int SEND_WALKABLE_INTERFACE = 208;

    public static final int SEND_MINIMAP_STATE = 99;

    public static final int SHOW_NPC_HEAD_ON_INTERFACE = 75;

    public static final int SEND_MULTIPLE_MAP_PACKETS = 60;

    public static final int SEND_EARTHQUAKE = 35;

    public static final int SEND_PLAYER_OPTION = 104;

    public static final int CLEAR_MINIMAP_FLAG = 78;

    public static final int SEND_MESSAGE = 253;

    public static final int STOP_ALL_ANIMATIONS = 1;

    public static final int ADD_SET_FRIEND = 50;

    public static final int SEND_RUN_ENERGY = 110;

    public static final int SEND_HINT_ICON = 254;

    public static final int SEND_DUO_INTERFACE = 248;

    public static final int SEND_RECEIVED_PRIVATE_MESSAGE = 196;

    public static final int SEND_REGION = 85;

    public static final int SEND_ITEM_TO_INTERFACE = 246;

    public static final int SHOW_HIDE_INTERFACE_CONTAINER = 171;

    public static final int SEND_SOLO_NON_WALKABLE_SIDEBAR_INTERFACE = 142;

    public static final int SET_INTERFACE_TEXT = 126;

    public static final int UPDATE_CHAT_MODES = 206;

    public static final int SEND_PLAYER_WEIGHT = 240;

    public static final int SEND_MODEL_TO_INTERFACE = 8;

    public static final int SEND_CHANGE_INTERFACE_COLOUR = 122;

    public static final int SEND_UPDATE_ITEMS = 53;

    public static final int SET_MODEL_INTERFACE_ZOOM = 230;

    public static final int SET_FRIENDSERVER_STATUS = 221;

    public static final int MOVE_CAMERA = 177;

    public static final int SEND_INITIALIZE_PACKET = 249;

    public static final int NPC_UPDATING = 65;

    public static final int SEND_ENTER_AMOUNT = 27;

    public static final int SEND_ENTER_NAME = 187;

    public static final int SEND_NON_WALKABLE_INTERFACE = 97;

    public static final int SEND_WALKABLE_CHATBOX_INTERFACE = 218;

    public static final int SEND_CONFIG_INT = 87;

    public static final int SEND_CONFIG_BYTE = 36;

    public static final int SEND_MULTICOMBAT_ICON = 61;

    public static final int SEND_ANIMATE_INTERFACE = 200;

    public static final int CLOSE_INTERFACE = 219;

    public static final int UPDATE_SPECIFIC_ITEM = 34;

    public static final int SWITCH_TAB = 106;

    public static final int SEND_NONWALKABLE_CHATBOX_INTERFACE = 164;

	public static final int SEND_MAP_REGION = 73;

	public static final int SEND_REGION_MAP_REGION = 241;
	
	public static final int MOUSE_CLICK = 241;
	
	public static final int SYSTEM_UPDATE = 114;
	
	public static final int PLAY_SOUND_EFFECT = 174;
	
	public static final int IDLE_LOGOUT = 202;
	
	public static final int ITEM_ON_NPC = 57;
	
	/**
	 * This array is interesting, it's never used and it has interesting properites.
	 * 
	 *  It's never used and has a lenght of 257.
	 *  
	 *  This is was used to map obfusticated packet opcodes with their actual opcodes.
	 *  
	 *  In 400+ they switched to using constants instead to make it more difficult.
	 */
	public static final int[] OBFUSTICATED_OPCODES = {			
			6, 21, 25, 33, 254, 127, 183, 87, 216, 215, 
			211, 48, 15, 195, 149, 233, 162, 102, 104, 179, 
			222, 103, 224, 81, 152, 89, 45, 11, 197, 187, 
			210, 37, 135, 220, 137, 128, 63, 188, 207, 144, 
			201, 161, 28, 192, 206, 32, 115, 57, 196, 22, 
			132, 226, 227, 169, 237, 105, 174, 109, 5, 55, 
			205, 156, 8, 34, 113, 176, 209, 3, 50, 117, 
			122, 189, 101, 142, 246, 163, 238, 76, 74, 84, 
			91, 217, 58, 23, 118, 66, 35, 164, 114, 138, 
			96, 110, 29, 235, 147, 249, 214, 198, 242, 56, 
			94, 248, 59, 253, 150, 16, 13, 46, 24, 130, 
			232, 153, 167, 229, 79, 134, 26, 191, 0, 213, 
			204, 241, 160, 39, 180, 49, 250, 47, 140, 193, 
			202, 108, 120, 247, 106, 194, 65, 27, 93, 143, 
			186, 171, 125, 54, 155, 190, 139, 165, 77, 178, 
			72, 99, 61, 141, 116, 100, 80, 184, 154, 145, 
			131, 12, 90, 42, 255, 75, 44, 78, 172, 107, 
			52, 7, 119, 146, 38, 218, 10, 223, 182, 240, 
			159, 88, 158, 64, 221, 200, 1, 43, 252, 62, 
			40, 230, 129, 18, 111, 51, 17, 53, 136, 20, 
			60, 225, 30, 9, 239, 97, 234, 41, 203, 236, 
			36, 185, 212, 19, 245, 251, 208, 175, 243, 86, 
			2, 69, 181, 151, 14, 166, 70, 98, 124, 126, 
			67, 157, 199, 112, 123, 177, 82, 168, 71, 170, 
			95, 31, 92, 4, 231, 219, 73, 85, 244, 148, 
			173, 228, 121, 83, 133, 68, 0
		};

		public static final int[] PACKET_SIZES = {
		            /*   0 -   9 */ 0, 0, 0, 0, 6, 0, 0, 0, 4, 0, 
		            /*  10 -  19 */ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		            /*  20 -  19 */ 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 
		            /*  30 -  19 */ 0, 0, 0, 0, -2, 4, 3, 0, 0, 0, 
		            /*  40 -  19 */ 0, 0, 0, 0, 5, 0, 0, 6, 0, 0, 
		            /*  50 -  19 */ 9, 0, 0, -2, 0, 0, 0, 0, 0, 0, 
		            /*  60 -  19 */ -2, 1, 0, 0, 2, -2, 0, 0, 0, 0, 
		            /*  70 -  19 */ 6, 3, 2, 4, 2, 4, 0, 0, 0, 4, 
		            /*  80 -  19 */ 0, -2, 0, 0, 7, 2, 0, 6, 0, 0, 
		            /*  90 -  19 */ 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 
		            /* 100 - 109 */ 0, 2, 0, 0, -1, 4, 1, 0, 0, 0, 
		            /* 110 - 119 */ 1, 0, 0, 0, 2, 0, 0, 15, 0, 0, 
		            /* 120 - 129 */ 0, 4, 6, 0, 0, 0, -2, 9, 0, 0, 
		            /* 130 - 139 */ 0, 0, 0, 0, 6, 0, 0, 1, -1, 4, 
		            /* 140 - 149 */ 0, 0, 2, 0, 0, 0, 0, 14, 0, 0, 
		            /* 150 - 159 */ 0, 4, 0, 0, 0, 0, 3, 0, 0, 0, 
		            /* 160 - 169 */ 4, 0, 0, 0, 2, 0, 6, 0, 0, 0, 
		            /* 170 - 179 */ 0, 3, 0, 0, 5, -1, 10, 6, 3, 0, 
		            /* 180 - 189 */ 0, 0, 1, 1, 0, 2, 0, 0, 0, 0, 
		            /* 190 - 199 */ 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 
		            /* 200 - 209 */ 4, 2, -1, 0, 8, 0, 3, 0, 2, 0, 
		            /* 210 - 219 */ 0, 0, 0, 0, -2, 7, 0, -2, 2, 0, 
		            /* 220 - 229 */ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 
		            /* 230 - 239 */ 8, 0, 0, 0, 0, 0, 0, 0, 0, 0, 
		            /* 240 - 249 */ 2, -2, 0, 0, 0, 0, 6, 0, 4, 3, 
		            /* 250 - 259 */ 0, 0, 0, -1, 6, 0, 0
		};

}
