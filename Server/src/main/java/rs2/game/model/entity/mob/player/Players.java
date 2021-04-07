
package rs2.game.model.entity.mob.player;

import rs2.game.model.entity.item.Item;
import rs2.game.model.entity.mob.MobAnimation;
import rs2.net.packet.out.DisplayChatBoxWidgetPacket;
import rs2.net.packet.out.SetItemOnInterfaceSlotPacket;
import rs2.net.packet.out.SetSideBarWidgetPacket;
import rs2.net.packet.out.SetWidgetStringPacket;

/**
 * A static-utility class that contains useful methods for {@link Player}s.
 * 
 * @author SeVen
 */
public class Players {

      private static final int ATTACK_TAB = 0;
      private static final int SKILL_TAB = 1;
      private static final int QUEST_TAB = 2;
      private static final int INVENTORY_TAB = 3;
      private static final int EQUIPMENT_TAB = 4;
      private static final int PRAYER_TAB = 5;
      private static final int MAGIC_TAB = 6;
      private static final int CLAN_TAB = 7;
      private static final int FRIENDS_TAB = 8;
      private static final int IGNORE_TAB = 9;
      private static final int LOGOUT_TAB = 10;
      private static final int WRENCH_TAB = 11;
      private static final int EMOTE_TAB = 12;
      private static final int MUSIC_TAB = 13;

      /**
       * Creates all the side-bar interfaces for a {@code player}.
       * 
       * @param player
       *    The player to create the side-bar interfaces for.
       *    
       * @param enable
       *    The flag that denotes to toggle these side-bar interfaces. 
       */
      public static void createSideBarInterfaces(Player player, boolean enable) {
            if (enable) {
                  player.queuePacket(new SetSideBarWidgetPacket(ATTACK_TAB, 2423));
                  player.queuePacket(new SetSideBarWidgetPacket(SKILL_TAB, 3917));
                  player.queuePacket(new SetSideBarWidgetPacket(QUEST_TAB, 638));
                  player.queuePacket(new SetSideBarWidgetPacket(INVENTORY_TAB, 3213));
                  player.queuePacket(new SetSideBarWidgetPacket(EQUIPMENT_TAB, 1644));
                  player.queuePacket(new SetSideBarWidgetPacket(PRAYER_TAB, 5608));
                  player.queuePacket(new SetSideBarWidgetPacket(MAGIC_TAB, 1151));
                  player.queuePacket(new SetSideBarWidgetPacket(CLAN_TAB, -1));
                  player.queuePacket(new SetSideBarWidgetPacket(FRIENDS_TAB, 5065));
                  player.queuePacket(new SetSideBarWidgetPacket(IGNORE_TAB, 5715));
                  player.queuePacket(new SetSideBarWidgetPacket(LOGOUT_TAB, 2449));
                  player.queuePacket(new SetSideBarWidgetPacket(WRENCH_TAB, 904));
                  player.queuePacket(new SetSideBarWidgetPacket(EMOTE_TAB, 147));
                  player.queuePacket(new SetSideBarWidgetPacket(MUSIC_TAB, 962));
            }
      }

      /**
       * Resets animations for a {@code player}.
       * 
       * @param player
       *    The player who's animations to reset.
       */
      public static void resetPlayerAnimation(Player player) {
            player.getMobAnimation().setWalk(MobAnimation.PLAYER_WALK);
            player.getMobAnimation().setStand(MobAnimation.PLAYER_STAND);
            player.getMobAnimation().setTurn(MobAnimation.PLAYER_TURN);
            player.getMobAnimation().setTurn180(MobAnimation.PLAYER_TURN_180);
            player.getMobAnimation().setTurn90CCW(MobAnimation.PLAYER_TURN_90_CCW);
            player.getMobAnimation().setTurn90CW(MobAnimation.PLAYER_TURN_90_CW);
            player.getMobAnimation().setRun(MobAnimation.PLAYER_RUN);
      }

      /**
       * Sends the destroy Item interface to a {@code player}.
       * 
       * @param player
       *    The player to destroy the item for.
       *    
       * @param item
       *    The item being destroyed.
       */
      public static void destroyItem(Player player, Item item) {
            player.queuePacket(new SetItemOnInterfaceSlotPacket(14171, item, 0));
            player.queuePacket(new SetWidgetStringPacket("Are you sure you want to drop this item?", 14174));
            player.queuePacket(new SetWidgetStringPacket("Yes.", 14175));
            player.queuePacket(new SetWidgetStringPacket("No.", 14176));
            player.queuePacket(new SetWidgetStringPacket("", 14177));
            player.queuePacket(new SetWidgetStringPacket("This item is valuable, you will not", 14182));
            player.queuePacket(new SetWidgetStringPacket("get it back once lost.", 14183));
            player.queuePacket(new SetWidgetStringPacket(item.getName(), 14184));
            player.queuePacket(new DisplayChatBoxWidgetPacket(14170));
      }



}
