package com.catapult.gui;

import com.catapult.managers.OsManager;
import com.catapult.managers.OsManagerFactory;
import com.catapult.monitor.Monitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class OptionsGui {
  private final static Logger LOG = LoggerFactory.getLogger(OptionsGui.class);
  private final OsManager osManager = OsManagerFactory.getOsManager();

  private final JFrame gui;

  public OptionsGui() {
    gui = new JFrame("Catapult");
    gui.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    gui.setAlwaysOnTop(true);
  }

  public void show() {
    Optional<Monitor> monitorMaybe = osManager.getMonitorWithMouse();
    if (monitorMaybe.isEmpty()) {
      throw new IllegalStateException("Could not find monitor with mouse");
    }

    Monitor monitor = monitorMaybe.get();
    LOG.info("Displaying GUI at monitor {}", monitor.getDisplayableMonitorNumber());

    Rectangle bounds = monitor.getBounds();

    gui.setLocationRelativeTo(new JFrame(monitor.getConfiguration()));
    gui.setSize(bounds.width / 3, bounds.height / 3);
    gui.setVisible(true);
  }

  public void close() {
    LOG.info("Closing Catapult GUI");
    gui.dispose();
  }
}
