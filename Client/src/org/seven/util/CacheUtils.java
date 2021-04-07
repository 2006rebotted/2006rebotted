package org.seven.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.runescape.Client;
import com.runescape.cache.FileStore.Store;
import com.runescape.sign.SignLink;

public final class CacheUtils {

      public static void repackCacheIndex(Client client, Store cacheIndex) {
            System.out.println("Started repacking index " + cacheIndex.getIndex() + ".");
            int indexLength = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles().length;
            File[] file = new File(SignLink.indexLocation(cacheIndex.getIndex(), -1)).listFiles();
            try {
                  for (int index = 0; index < indexLength; index++) {
                        int fileIndex = Integer.parseInt(
                                    Client.getFileNameWithoutExtension(file[index].toString()));
                        byte[] data = FileUtils.fileToByteArray(cacheIndex.getIndex(), fileIndex);
                        if (data != null && data.length > 0) {
                              client.indices[cacheIndex.getIndex()].writeFile(data.length, data, fileIndex);
                              System.out.println("Repacked " + fileIndex + ".");
                        } else {
                              System.out.println("Unable to locate index " + fileIndex + ".");
                        }
                  }
            } catch (Exception ex) {                  
                  System.out.println("Error packing cache index " + cacheIndex.getIndex() + ".");
            }
            System.out.println("Finished repacking " + cacheIndex.getIndex() + ".");
      }
      
	public static String getFileNameWithoutExtension(String fileName) {
          File tmpFile = new File(fileName);
          tmpFile.getName();
          int whereDot = tmpFile.getName().lastIndexOf('.');
          if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
                return tmpFile.getName().substring(0, whereDot);
          }
          return "";
    }

      public static void dumpCacheIndex(Client client, Store cacheIndex) {            
            try {
                  for (int i = 0;; i++) {
                        try {
                              byte[] indexByteArray = client.indices[cacheIndex.getIndex()].decompress(i);
                              if (indexByteArray == null) {
                                    System.out.println("Finished dumping index " + cacheIndex.getIndex()
                                                + ", exiting dump operation.");
                                    break;
                              }
                              
                              final File dir = new File(SignLink.findcachedir() + "dump" + cacheIndex.getIndex() + "/");
                              
                              if (!dir.exists()) {
                            	  dir.mkdirs();
                              }
                              
                              BufferedOutputStream gzip = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(SignLink.findcachedir() + "dump" + cacheIndex.getIndex() + "/" + i + ".gz")));
                              
                              if (indexByteArray.length == 0) {
                                    continue;
                              } else {
                                    gzip.write(indexByteArray);
                                    System.out.println("Unpacked " + i + ".");
                                    gzip.close();

                              }
                        } catch (IOException ex) {                              
                              throw new IOException(
                                          "Error writing to folder. Ensure you have this directory created: '"
                                                      + SignLink.findcachedir() + "dump"
                                                      + cacheIndex.getIndex() + "'");
                        }
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }
      }

}
