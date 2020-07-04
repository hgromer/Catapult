package com.catapult.monitor;

import java.awt.*;

public class Monitor {
  private final GraphicsDevice device;
  private final int monitorNumber;

  Monitor(GraphicsDevice device, int monitorNumber) {
    this.device = device;
    this.monitorNumber = monitorNumber;
  }

  public String getName() {
    return device.getIDstring();
  }

  public Rectangle getBounds() {
    return device.getDefaultConfiguration()
        .getBounds();
  }

  public int getDisplayableMonitorNumber() {
    return monitorNumber + 1;
  }

  public GraphicsConfiguration getConfiguration() {
    return device.getDefaultConfiguration();
  }

  @Override
  public String toString() {
    return getName();
  }
}
