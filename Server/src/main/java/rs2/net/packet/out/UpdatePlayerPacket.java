package rs2.net.packet.out;

import rs2.game.model.Position;
import rs2.game.model.World;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.player.update.mask.*;
import rs2.game.model.entity.mob.update.UpdateBlock;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.AccessType;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Iterator;
import java.util.Optional;

/**
 * The packet for updating a player.
 * 
 * @author Vult-R
 */
public final class UpdatePlayerPacket implements Sendable {

      /**
       * The max amount of players that can be added per cycle.
       */
      private static final int MAX_NEW_PLAYERS_PER_CYCLE = 20;

      @Override
      public Optional<OutgoingPacket> writePacket(Player player) {
            if (player.isRegionChange() || player.isTeleporting()) {
                  player.queuePacket(new UpdateMapRegion());
            }

            GamePacketBuilder builder = new GamePacketBuilder(81, PacketHeader.VARIABLE_SHORT);
            
            GamePacketBuilder update = new GamePacketBuilder();

            builder.initializeAccess(AccessType.BIT);

            updatePlayerMovement(player, builder);

            if (player.isUpdateRequired()) {
                  appendUpdates(player, update, false, true);
            }

            builder.writeBits(8, player.getLocalPlayers().size());

            for (final Iterator<Player> iterator = player.getLocalPlayers().iterator(); iterator
                        .hasNext();) {

                  final Player other = iterator.next();

                  if (World.world.getPlayers().get(other.getSlot()) != null && other.isRegistered()
                              && !other.isTeleporting() && other.getPosition().isWithinDistance(
                                          player.getPosition(), Position.VIEWING_DISTANCE)) {

                        updateOtherPlayerMovement(other, builder);

                        if (other.isUpdateRequired()) {
                              appendUpdates(other, update, false, false);
                        }
                  } else {
                        iterator.remove();
                        builder.writeBit(true);
                        builder.writeBits(2, 3);
                  }
            }

            int playersAdded = 0;

            for (final Player other : World.world.getPlayers()) {
                  if (other == null || !other.isRegistered() || other == player
                              || player.getLocalPlayers().contains(other)) {
                        continue;
                  }

                  if (other.getPosition().isWithinDistance(player.getPosition(),
                              Position.VIEWING_DISTANCE)) {

                        if (player.getLocalPlayers().size() >= 255
                                    || playersAdded > MAX_NEW_PLAYERS_PER_CYCLE) {
                              break;
                        }

                        addPlayer(player, other, builder);
                        appendUpdates(other, update, true, false);
                        playersAdded++;
                  }
            }

            if (update.buffer().writerIndex() > 0) {
                  builder.writeBits(11, 2047);
                  builder.initializeAccess(AccessType.BYTE);
                  builder.writeBytes(update.buffer());
            } else {
                  builder.initializeAccess(AccessType.BYTE);
            }
            return builder.toOutgoingPacket();
      }

      /**
       * Adds a players to the local player list, and in-view of other players.
       * 
       * @param player The player to add.
       * 
       * @param other The other player that will be viewing this player.
       * 
       * @param builder The buffer that will be used to store data.
       */
      private void addPlayer(Player player, Player other, GamePacketBuilder builder) {
            player.getLocalPlayers().add(other);
            builder.writeBits(11, other.getSlot()).writeBit(true).writeBit(true)
                        .writeBits(5, player.getPosition().getDeltaY(other))
                        .writeBits(5, player.getPosition().getDeltaX(other));
      }

      /**
       * Appends pieces of a players update block to the main player update block.
       * 
       * @param block The update block to append.
       * 
       * @param player The player to append the update for.
       * 
       * @param builder The buffer to store data.
       */
      private void append(UpdateBlock<Player> block, Player player, GamePacketBuilder builder) {
            block.encode(player, builder);
      }

      /**
       * Updates the state of a player.
       * 
       * @param player The player to update the state for.
       * 
       * @param builder The buffer that the data will be written to.
       */
      private void appendUpdates(Player player, GamePacketBuilder builder,
                  boolean forceAppearence, boolean noChat) {
            if (!player.isUpdateRequired() && !forceAppearence) {
                  return;
            }

            int mask = 0x0;

            if (player.getUpdateFlags().contains(UpdateFlag.FORCE_MOVEMENT)) {
                  mask |= 0x400;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.GRAPHICS)) {
                  mask |= 0x100;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.ANIMATION)) {
                  mask |= 0x8;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FORCED_CHAT)
                        && player.getForcedChat().length() > 0) {
                  mask |= 0x4;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.CHAT) && !noChat) {
                  mask |= 0x80;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.APPEARANCE) || forceAppearence) {
                  mask |= 0x10;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.ENTITY_INTERACTION)) {
                  mask |= 0x1;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FACE_COORDINATE)) {
                  mask |= 0x2;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.SINGLE_HIT)) {
                  mask |= 0x20;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.DOUBLE_HIT)) {
                  mask |= 0x200;
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FORCE_MOVEMENT)) {
                  mask |= 0x400;
            }

            if (mask >= 0x100) {
                  mask |= 0x40;
                  builder.write(mask & 0xFF);
                  builder.write(mask >> 8);
            } else {
                  builder.write(mask);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.GRAPHICS)) {
                  append(new PlayerGraphicUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.ANIMATION)) {
                  append(new PlayerAnimationUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FORCED_CHAT)
                        && player.getForcedChat().length() > 0) {
                  append(new PlayerForceChatUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.CHAT) && !noChat) {
                  append(new PlayerChatUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.ENTITY_INTERACTION)) {
                  append(new PlayerInteractionUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.APPEARANCE) || forceAppearence) {
                  append(new PlayerAppearanceUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FACE_COORDINATE)) {
                  append(new PlayerFaceCoordinateUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.SINGLE_HIT)) {
                  append(new PlayerSingleHitUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.DOUBLE_HIT)) {
                  append(new PlayerDoubleHitUpdateBlock(), player, builder);
            }

            if (player.getUpdateFlags().contains(UpdateFlag.FORCE_MOVEMENT)) {
                  append(new PlayerForceMovementUpdateBlock(), player, builder);
            }

      }

      /**
       * Handles updating a players movement.
       * 
       * @param player The player to update.
       * 
       * @param builder The buffer to store data.
       */
      private void updatePlayerMovement(Player player, GamePacketBuilder builder) {

    	  	/*
             * Check if the player is teleporting.
             */
            if (player.isTeleporting() || player.isRegionChange()) {
            	
                  /*
                   * They are, so an update is required.
                   */
                  builder.writeBit(true);

                  /*
                   * This value indicates the player teleported.
                   */
                  builder.writeBits(2, 3);

                  /*
                   * This is the new player height.
                   */
                  builder.writeBits(2, player.getPosition().getHeight());

                  /*
                   * This indicates that the client should discard the walking queue.
                   */
                  builder.writeBits(1, player.isTeleporting() ? 1 : 0);

                  /*
                   * This flag indicates if an update block is appended.
                   */
                  builder.writeBits(1, player.isUpdateRequired() ? 1 : 0);

                  /*
                   * These are the positions.
                   */
                  builder.writeBits(7, player.getPosition().getLocalY(player.getLastLocation()));
                  builder.writeBits(7, player.getPosition().getLocalX(player.getLastLocation()));
                  return;
            } else {

                  /*
                   * Otherwise, check if the player moved.
                   */
                  if (player.getWalkingDirection() == -1) {
                        /*
                         * The player didn't move. Check if an update is required.
                         */
                        if (player.isUpdateRequired()) {
                              /*
                               * Signifies an update is required.
                               */
                              builder.writeBit(true);

                              /*
                               * But signifies that we didn't move.
                               */
                              builder.writeBits(2, 0);
                        } else {
                              /*
                               * Signifies that nothing changed.
                               */
                              builder.writeBit(false);
                        }
                  } else {
                	  
                        /*
                         * Check if the player was running.
                         */
                        if (player.getRunningDirection() == -1) { 
                        	
                              /*
                               * The player walked, an update is required.
                               */
                              builder.writeBit(true);

                              /*
                               * This indicates the player only walked.
                               */
                              builder.writeBits(2, 1);

                              /*
                               * This is the player's walking direction.
                               */
                              builder.writeBits(3, player.getWalkingDirection());

                              /*
                               * This flag indicates an update block is appended.
                               */
                              builder.writeBits(1,
                                          player.isUpdateRequired() ? 1 : 0);
                        } else {  
                        	
                              /*
                               * The player ran, so an update is required.
                               */
                              builder.writeBit(true);

                              /*
                               * This indicates the player ran.
                               */
                              builder.writeBits(2, 2);

                              /*
                               * This is the walking direction.
                               */
                              builder.writeBits(3, player.getWalkingDirection());

                              /*
                               * And this is the running direction.
                               */
                              builder.writeBits(3, player.getRunningDirection());

                              /*
                               * And this flag indicates an update block is appended.
                               */
                              builder.writeBits(1,
                                          player.isUpdateRequired() ? 1 : 0);
                        }
                  }
            }
      }

      /**
       * Updates a non-this player's movement.
       * 
       * @param builder The packet.
       * @param other The other player to update.
       */
      private void updateOtherPlayerMovement(Player other, GamePacketBuilder builder) {
    	  /*
             * Check which type of movement took place.
             */
            if (other.getWalkingDirection() == -1) {
                  /*
                   * If no movement did, check if an update is required.
                   */
                  if (other.isUpdateRequired()) {
                        /*
                         * Signify that an update happened.
                         */
                        builder.writeBit(true);

                        /*
                         * Signify that there was no movement.
                         */
                        builder.writeBits(2, 0);
                  } else {
                        /*
                         * Signify that nothing changed.
                         */
                        builder.writeBit(false);
                  }
            } else if (other.getRunningDirection() == -1) {
                  /*
                   * The player moved but didn't run. Signify that an update is required.
                   */
                  builder.writeBit(true);

                  /*
                   * Signify we moved one tile.
                   */
                  builder.writeBits(2, 1);

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                  builder.writeBits(3, other.getWalkingDirection());

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                  builder.writeBit(other.isUpdateRequired());
            } else {
                  /*
                   * The player ran. Signify that an update happened.
                   */
                  builder.writeBit(true);

                  /*
                   * Signify that we moved two tiles.
                   */
                  builder.writeBits(2, 2);

                  /*
                   * Write the primary sprite (i.e. walk direction).
                   */
                  builder.writeBits(3, other.getWalkingDirection());

                  /*
                   * Write the secondary sprite (i.e. run direction).
                   */
                  builder.writeBits(3, other.getRunningDirection());

                  /*
                   * Write a flag indicating if a block update happened.
                   */
                  builder.writeBit(other.isUpdateRequired());
            }
      }

}
