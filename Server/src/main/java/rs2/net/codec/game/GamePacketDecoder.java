package rs2.net.codec.game;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import rs2.net.NetworkConstants;
import rs2.net.codec.IsaacCipher;
import rs2.net.codec.ProtocolConstants;
import rs2.net.packet.IncomingPacket;
import rs2.net.packet.PacketHeader;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

/**
 * The {@link ByteToMessageDecoder} implementation that decodes incoming {@link IncomingPacket}s.
 * 
 * @author Vult-R
 */
public final class GamePacketDecoder extends ByteToMessageDecoder {

      /**
       * The decryptor used to decode the {@link IncomingPacket}.
       */
      private final IsaacCipher decryptor;

      /**
       * The default opcode of this packet.
       */
      private int opcode = ProtocolConstants.DEFAULT_OPCODE;

      /**
       * The default size of this packet.
       */
      private int size = ProtocolConstants.VARIABLE_BYTE;

      /**
       * The default header for this packet.
       */
      private PacketHeader header = PacketHeader.EMPTY;

      /**
       * The current state this {@link GamePacketDecoder} is in.
       */
      private State state = State.OPCODE;

      /**
       * The container that holds the current {@link IncomingPacket}.
       */
      private Optional<IncomingPacket> currentPacket = Optional.empty();

      /**
       * Creates a new {@link GamePacketDecoder}
       * 
       * @param decryptor
       *    The decrypting {@link IsaacCipher}.
       */
      public GamePacketDecoder(IsaacCipher decryptor) {
            this.decryptor = decryptor;
      }

      @Override
      protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
                  throws Exception {
            switch (state) {

                  case OPCODE:
                        decodeOpcode(in);
                        break;

                  case SIZE:
                        decodeHeader(in);
                        break;

                  case PAYLOAD:
                        decodePayload(in);
                        break;

            }

            currentPacket.ifPresent($it -> {
                  out.add($it);
                  currentPacket = Optional.empty();
            });

      }

      /**
       * Decodes the opcode for this packet.
       *
       * @param in
       *    The incoming buffer.
       */
      private void decodeOpcode(ByteBuf in) {
            if (in.isReadable()) {
                  opcode = (in.readByte() - decryptor.getKey() & 0xFF);
                  size = NetworkConstants.PACKET_SIZES[opcode];

                  if (size == ProtocolConstants.VARIABLE_BYTE) {
                        header = PacketHeader.VARIABLE_BYTE;
                  } else if (size == ProtocolConstants.VARIABLE_SHORT) {
                        header = PacketHeader.VARIABLE_SHORT;
                  } else {
                        header = PacketHeader.FIXED;
                  }
            }

            if (size == 0) {
                  queuePacket(Unpooled.EMPTY_BUFFER);
                  return;
            }

            state = size == ProtocolConstants.VARIABLE_BYTE
                        || size == ProtocolConstants.VARIABLE_SHORT ? State.SIZE : State.PAYLOAD;
      }

      /**
       * Decodes the size/header for this packet.
       * 
       * @param in
       *    The incoming buffer.
       */
      private void decodeHeader(ByteBuf in) {
            int bytes = size == -1 ? Byte.BYTES : Short.BYTES;

            if (in.isReadable(bytes)) {

                  size = 0;

                  for (int index = 0; index < bytes; index++) {
                        size |= in.readUnsignedByte() << 8 * (bytes - 1 - index);
                  }
                  state = State.PAYLOAD;
            }

      }

      /**
       * Decodes this packets payload.
       * 
       * @param in
       *    The incoming buffer.
       */
      private void decodePayload(ByteBuf in) {
            if (in.isReadable(size)) {
                  queuePacket(in.readBytes(size));
            }
      }

      /**
       * Queues an incoming packet to be sent to be handled upstream.
       * 
       * @param payload
       *    The buffer containing this packets payload.
       */
      private void queuePacket(ByteBuf payload) {
            checkState(opcode >= 0, "opcode < 0");
            checkState(size >= 0, "size < 0");
            checkState(header != PacketHeader.EMPTY, "type == PacketHeader.EMPTY");
            try {
                  currentPacket = Optional.of(new IncomingPacket(opcode, header, payload));
            } finally {
                  opcode = ProtocolConstants.DEFAULT_OPCODE;
                  size = ProtocolConstants.VARIABLE_BYTE;
                  state = State.OPCODE;
            }
      }

      /**
       * Represents the enumerated parts of a {@link IncomingPacket} that needs
       * to be decoded.
       */
      private enum State {
            /**
             * The state that determines the id of this packet.
             */
            OPCODE,

            /**
             * The state that determines the size of this packet.
             */
            SIZE,

            /**
             * The state that contains the data of the packet.
             */
            PAYLOAD;
      }

}
