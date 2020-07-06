package com.catapult.gui;

import com.catapult.listener.GlobalScreenManager;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CatapultSystemTray {
  private static final Logger LOG = LoggerFactory.getLogger(CatapultSystemTray.class);

  private final SystemTray systemTray = SystemTray.getSystemTray();
  private final PopupMenu popupMenu = new PopupMenu();
  private final TrayIcon icon =
      new TrayIcon(Toolkit.getDefaultToolkit().getImage("/Users/hernan/repos/Catapult/src/main/resources/catapult.png"));

  public CatapultSystemTray() {
    for (MenuItem item : buildMenuItems()) {
      popupMenu.add(item);
    }

    icon.setPopupMenu(popupMenu);
    try {
      systemTray.add(icon);
      LOG.info("System tray built");
    } catch (AWTException e) {
      LOG.error("Error building system tray", e);
      throw new IllegalStateException("Could not build tray");
    }
  }

  public void close() {
    systemTray.remove(icon);
  }

  private ImmutableList<MenuItem> buildMenuItems() {
    MenuItem quitItem = new MenuItem("Quit");

    quitItem.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        LOG.info("System tray quit button selected");
        GlobalScreenManager.unregisterNativeHook();
        LOG.info("Gracefully shutting down Catapult...");
        System.exit(0);
      }
    });
    return ImmutableList.of(quitItem);
  }
}
