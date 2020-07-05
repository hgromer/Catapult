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
    Rectangle bounds = monitor.getBounds();

    JLabel text = new JLabel(String.valueOf(monitor.getDisplayableMonitorNumber()), SwingConstants.CENTER);
    text.setFont(new Font("Lucida Grande", Font.BOLD,40));
    text.setOpaque(true);
    text.setBackground(Color.BLACK);
    text.setForeground(Color.WHITE);

    this.frame.setLocationRelativeTo(new JFrame(monitor.getConfiguration()));
    this.frame.setUndecorated(true);
    this.frame.setAlwaysOnTop(true);
    this.frame.add(text);
    this.frame.pack();
    this.frame.setSize(bounds.width / 10, bounds.height / 10); // todo - this looks meh?
    this.frame.setFocusableWindowState(false);
  }

  public void display() {
    frame.setVisible(true);
  }

  public void kill() {
    frame.dispose();
  }
}
