package com.catapult.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class MonitorInfoDisplay {
  private static final Logger LOG = LoggerFactory.getLogger(MonitorInfoDisplay.class);

  private final JFrame frame;

  public MonitorInfoDisplay(Monitor monitor) {
    LOG.info("Building MonitorInfoDisplay for monitor {} with bounds {}",
        monitor.getDisplayableMonitorNumber(), monitor.getBounds());

    this.frame = new JFrame();

    JLabel text = new JLabel(String.valueOf(monitor.getDisplayableMonitorNumber()), SwingConstants.CENTER);
    text.setFont(new Font("Verdana", Font.BOLD,20));
    Rectangle bounds = monitor.getBounds();

    this.frame.setLocation(bounds.x, bounds.y);
    this.frame.setUndecorated(true);
    this.frame.setAlwaysOnTop(true);
    this.frame.add(text);
    this.frame.pack();
    this.frame.setSize(100, 100);
    this.frame.setFocusableWindowState(false);
  }

  public void display() {
    frame.setVisible(true);
  }

  public void hide() {
    frame.setVisible(false);
  }

  public void kill() {
    frame.dispose();
  }
}
