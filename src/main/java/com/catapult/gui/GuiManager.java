package com.catapult.gui;

public class GuiManager {
  private static final OptionsGui OPTIONS_GUI = new OptionsGui();
  private static final CatapultSystemTray CATAPULT_SYSTEM_TRAY = new CatapultSystemTray();

  public static OptionsGui getOptionsGui() {
    return OPTIONS_GUI;
  }

  public static CatapultSystemTray getCatapultSystemTray() {
    return CATAPULT_SYSTEM_TRAY;
  }
}
