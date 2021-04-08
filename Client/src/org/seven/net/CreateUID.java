package org.seven.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public final class CreateUID {

      private static final String PATH =
                  System.getProperty("user.home") + File.separator + "2006Scape_UID.dat";

      public static String generateUID() throws Exception {
            String uuid = getUUID();
            try {

                  File dir = new File(PATH);
                  
                  if (!dir.exists()) {                        
                        write(uuid);
                        return uuid;
                  }
                  
                  read(uuid);

            } catch (Exception e) {
                  e.printStackTrace();
            }
            return uuid;
      }
      
      private static void write(String data) throws FileNotFoundException, IOException{
            try(DataOutputStream output = new DataOutputStream(new FileOutputStream(PATH))) {
                  output.writeUTF(data);
                  output.flush();
            }
      }
      
      private static void read(String data) throws FileNotFoundException, IOException {            
            try(DataInputStream input = new DataInputStream(new FileInputStream(PATH))) {
                  data = input.readUTF();
            }
      }

      private static String getUUID() {            
            return UUID.randomUUID().toString();
      }
      
}
