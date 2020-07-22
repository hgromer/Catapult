package com.catapult.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsManagerFactory {
  private static final Logger LOG = LoggerFactory.getLogger(OsManagerFactory.class);
  private static final String OS = System.getProperty("os.name");
  private static final OsManager OS_MANAGER;

  static {
    if (isMac()) {
      LOG.info("OS is MacOs");
      LOG.info("Registering {} as OS manager", MacOsManager.class);
      System.setProperty("apple.awt.UIElement", "true");
      OS_MANAGER = new MacOsManager();
    } else {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
  }

  public static OsManager getOsManager() {
    return OS_MANAGER;
  }

  public static boolean isMac() {
    return OS.startsWith("Mac");
  }
}
