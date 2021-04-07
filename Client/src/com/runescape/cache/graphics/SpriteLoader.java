package com.runescape.cache.graphics;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.seven.util.FileUtils;

import com.runescape.io.Buffer;
import com.runescape.sign.SignLink;
import com.runescape.util.LoggerUtils;

public final class SpriteLoader {

      private static final Logger LOGGER = LoggerUtils.getLogger(SpriteLoader.class);

      public static SpriteLoader[] cache;
      private static Sprite[] sprites = null;
      public static int totalSprites;
      public String name;
      public int id;
      public int drawOffsetX;
      public int drawOffsetY;
      public byte[] spriteData;

      public SpriteLoader() {
            name = "Unknown";
            id = -1;
            drawOffsetX = 0;
            drawOffsetY = 0;
            spriteData = null;
      }
      
      public static void loadSprites() {
            try {
                  Buffer index = new Buffer(FileUtils
                              .readFile(SignLink.findcachedir() + "sprites.idx"));
                  Buffer data = new Buffer(FileUtils
                              .readFile(SignLink.findcachedir() + "sprites.dat"));
                  DataInputStream indexFile = new DataInputStream(
                              new GZIPInputStream(new ByteArrayInputStream(index.payload)));
                  DataInputStream dataFile = new DataInputStream(
                              new GZIPInputStream(new ByteArrayInputStream(data.payload)));
                  int totalSprites = indexFile.readInt();
                  LOGGER.info("Sprites Loaded: " + totalSprites);
                  if (cache == null) {
                        cache = new SpriteLoader[totalSprites];
                        sprites = new Sprite[totalSprites];
                  }
                  for (int i = 0; i < totalSprites; i++) {
                        int id = indexFile.readInt();
                        if (cache[id] == null) {
                              cache[id] = new SpriteLoader();
                        }
                        cache[id].readValues(indexFile, dataFile);
                        createSprite(cache[id]);
                  }
                  indexFile.close();
                  dataFile.close();
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

      public void readValues(DataInputStream index, DataInputStream data) throws IOException {
            do {
                  int opCode = data.readByte();
                  if (opCode == 0) {
                        break;
                  }
                  if (opCode == 1) {
                        id = data.readShort();
                  } else if (opCode == 2) {
                        name = data.readUTF();
                  } else if (opCode == 3) {
                        drawOffsetX = data.readShort();
                  } else if (opCode == 4) {
                        drawOffsetY = data.readShort();
                  } else if (opCode == 5) {
                        int indexLength = index.readInt();
                        byte[] dataread = new byte[indexLength];
                        data.readFully(dataread);
                        spriteData = dataread;
                  }
            } while (true);
      }

      public static void createSprite(SpriteLoader sprite) {
            sprites[sprite.id] = new Sprite(sprite.spriteData);
            sprites[sprite.id].drawOffsetX = sprite.drawOffsetX;
            sprites[sprite.id].drawOffsetY = sprite.drawOffsetY;
      }

      public static String getName(int index) {
            if (cache[index].name != null) {
                  return cache[index].name;
            } else {
                  return "null";
            }
      }

      public static int getOffsetX(int index) {
            return cache[index].drawOffsetX;
      }

      public static int getOffsetY(int index) {
            return cache[index].drawOffsetY;
      }

      public static Sprite[] getSprites() {
            return sprites;
      }

}
