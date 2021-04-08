package rs2.net.packet.out;

import rs2.Configuration;
import rs2.game.model.Position;
import rs2.game.model.World;
import rs2.game.model.entity.mob.npc.Npc;
import rs2.game.model.entity.mob.npc.updating.NpcUpdateBlock;
import rs2.game.model.entity.mob.npc.updating.mask.*;
import rs2.game.model.entity.mob.player.Player;
import rs2.game.model.entity.mob.update.UpdateFlag;
import rs2.net.codec.AccessType;
import rs2.net.codec.game.GamePacketBuilder;
import rs2.net.packet.OutgoingPacket;
import rs2.net.packet.PacketHeader;
import rs2.net.packet.Sendable;

import java.util.Iterator;
import java.util.Optional;

/**
 * The {@link OutgoingPacket} that updates a {@link Npc} in the game world.
 * 
 * @author SeVen
 */
public final class UpdateNpcPacket implements Sendable {

      @Override
      public Optional<OutgoingPacket> writePacket(Player player) {
            GamePacketBuilder builder = new GamePacketBuilder(65, PacketHeader.VARIABLE_SHORT);
            GamePacketBuilder update = new GamePacketBuilder();

            builder.initializeAccess(AccessType.BIT);

            builder.writeBits(8, player.getLocalNpcs().size());

            for (final Iterator<Npc> iterator = player.getLocalNpcs().iterator(); iterator
                        .hasNext();) {

                  final Npc mob = iterator.next();

                  if (World.world.getMobs().get(mob.getSlot()) != null && mob.isRegistered()
                              && player.getPosition().isWithinDistance(mob.getPosition(),
                                          Position.VIEWING_DISTANCE)) {
                        updateMovement(mob, builder);
                        if (mob.isUpdateRequired()) {
                              appendUpdates(mob, update);
                        }
                  } else {
                        iterator.remove();
                        builder.writeBit(true);
                        builder.writeBits(2, 3);
                  }
            }

            for (final Npc mob : World.world.getMobs()) {

                  if (player.getLocalNpcs().size() >= 255) {
                        break;
                  }

                  if (mob == null || player.getLocalNpcs().contains(mob) || !mob.isVisible()
                              || !mob.isRegistered()) {
                        continue;
                  }

                  if (mob.getPosition().isWithinDistance(player.getPosition(),
                              Position.VIEWING_DISTANCE)) {

                        addNPC(player, mob, builder);

                        if (mob.isUpdateRequired()) {
                              appendUpdates(mob, update);
                        }
                  }
            }
            if (update.buffer().writerIndex() > 0) {
                  builder.writeBits(14, 16383);
                  builder.initializeAccess(AccessType.BYTE);
                  builder.writeBuffer(update.buffer());
            } else {
                  builder.initializeAccess(AccessType.BYTE);
            }
            return builder.toOutgoingPacket();
      }


      /**
       * Adds a npc to a players local npc list to be viewable by a player.
       * 
       * @param mob The mob to add.
       * 
       * @param builder The buffer to store data.
       */
      public static void addNPC(Player player, Npc mob, GamePacketBuilder builder) {
            final int slot = mob.getSlot();
            player.getLocalNpcs().add(mob);
            builder.writeBits(14, slot);
            builder.writeBits(5, mob.getPosition().getY() - player.getPosition().getY());
            builder.writeBits(5, mob.getPosition().getX() - player.getPosition().getX());
            builder.writeBits(1, mob.isUpdateRequired() ? 1 : 0);
            builder.writeBits(Configuration.NPC_BITS, mob.getId());
            builder.writeBit(true);
      }

      /**
       * Appends a single {@link Npc}s update block to the main update block.
       * 
       * @param block The block to append.
       * 
       * @param mob The mob to update.
       * 
       * @param builder The buffer to store data.
       */
      public static void append(NpcUpdateBlock block, Npc mob, GamePacketBuilder builder) {
            block.encode(mob, builder);
      }

      /**
       * Updates the state of a mob.
       * 
       * @param mob The mob to update the state for.
       * 
       * @param builder The buffer that the data will be written to.
       */
      public static void appendUpdates(Npc mob, GamePacketBuilder builder) {

            int updateMask = 0x0;

            if (mob.getUpdateFlags().contains(UpdateFlag.ANIMATION)) {
                  updateMask |= 0x10;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.DOUBLE_HIT)) {
                  updateMask |= 0x8;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.GRAPHICS)) {
                  updateMask |= 0x80;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.ENTITY_INTERACTION)) {
                  updateMask |= 0x20;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.FORCED_CHAT)
                        && mob.getForcedChat().length() > 0) {
                  updateMask |= 0x1;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.SINGLE_HIT)) {
                  updateMask |= 0x40;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.TRANSFORM)) {
                  updateMask |= 0x2;
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.FACE_COORDINATE)) {
                  updateMask |= 0x4;
            }

            builder.write(updateMask);

            if (mob.getUpdateFlags().contains(UpdateFlag.ANIMATION)) {
                  append(new NpcAnimationUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.DOUBLE_HIT)) {
                  append(new NpcDoubleHitUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.GRAPHICS)) {
                  append(new NpcGraphicsUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.ENTITY_INTERACTION)) {
                  append(new NpcInteractionUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.FORCED_CHAT)
                        && mob.getForcedChat().length() > 0) {
                  append(new NpcForceChatUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.SINGLE_HIT)) {
                  append(new NpcSingleHitUpdateBlock(), mob, builder);
            }

            if (mob.getUpdateFlags().contains(UpdateFlag.FACE_COORDINATE)) {
                  append(new NpcFaceCoordinateUpdateBlock(), mob, builder);
            }
      }

      /**
       * Handles the update of a {@link Npc}'s movement.
       * 
       * @param mob The mob to update.
       * 
       * @param builder The buffer to store data.
       */
      public static void updateMovement(Npc mob, GamePacketBuilder builder) {
            if (mob.getWalkingDirection() == -1) {
                  if (mob.isUpdateRequired()) {
                        builder.writeBit(true);
                        builder.writeBits(2, 0);
                  } else {
                        builder.writeBits(1, 0);
                  }
            } else {
                  builder.writeBit(true);
                  builder.writeBits(2, 1);
                  builder.writeBits(3, mob.getWalkingDirection());
                  builder.writeBits(1, mob.isUpdateRequired() ? 1 : 0);
            }
      }

}
