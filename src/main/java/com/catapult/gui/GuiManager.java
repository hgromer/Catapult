package com.catapult.gui;

import com.catapult.Platform;

public class GuiManager {
  private static final OptionsGui OPTIONS_GUI = new OptionsGui();
  private static final CatapultSystemTray CATAPULT_SYSTEM_TRAY = new CatapultSystemTray();

  static {
    if (Platform.isMac()) {
      System.setProperty("apple.awt.UIElement", "true");
    } else {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
  }

  public static OptionsGui getOptionsGui() {
    return OPTIONS_GUI;
  }

  public static CatapultSystemTray getCatapultSystemTray() {
    return CATAPULT_SYSTEM_TRAY;
  }
}
