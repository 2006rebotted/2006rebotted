package com.runescape.util;

import java.lang.reflect.Method;

public final class MiscUtils {

      public static void launchURL(String url) {
            String osName = System.getProperty("os.name");
            try {
                  if (osName.startsWith("Mac OS")) {
                        Class<?> fileMgr = Class.forName("com.apple.eio.FileManager");
                        Method openURL = fileMgr.getDeclaredMethod("openURL",
                                    new Class[] {String.class});
                        openURL.invoke(null, new Object[] {url});
                  } else if (osName.startsWith("Windows"))
                        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                  else {
                        String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla",
                                    "netscape", "safari"};
                        String browser = null;
                        for (int count = 0; count < browsers.length && browser == null; count++)
                              if (Runtime.getRuntime().exec(new String[] {"which", browsers[count]})
                                          .waitFor() == 0)
                                    browser = browsers[count];
                        if (browser == null) {
                              throw new Exception("Could not find web browser");
                        } else
                              Runtime.getRuntime().exec(new String[] {browser, url});
                  }
            } catch (Exception ex) {
                  ex.printStackTrace();
            }
      }

}
