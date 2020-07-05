package com.catapult.managers;

import com.catapult.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OsManagerFactory {
  private static final Logger LOG = LoggerFactory.getLogger(OsManagerFactory.class);
  private static final OsManager OS_MANAGER;

  static {
    if (Platform.isMac()) {
      LOG.info("Registering {} as OS manager", MacOsManager.class);
      OS_MANAGER = new MacOsManager();
    } else {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
  }

  public static OsManager getOsManager() {
    return OS_MANAGER;
  }
}
