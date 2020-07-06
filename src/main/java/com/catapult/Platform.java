package com.catapult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Platform {
  private static final Logger LOG = LoggerFactory.getLogger(Platform.class);
  private static final String OS = System.getProperty("os.name");

  public static void setup() {
    if (isMac()) {
      LOG.info("Setting up Application for MacOs");
      System.setProperty("apple.awt.UIElement", "true");
    } else {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
  }

  public static boolean isMac() {
    return OS.startsWith("Mac");
  }
}
