package com.catapult.monitor;

import java.awt.*;

public class Monitor {
  private final GraphicsDevice device;

  Monitor(GraphicsDevice device) {
    this.device = device;
  }

  public String getName() {
    return device.getIDstring();
  }

  public Rectangle getBounds() {
    return device.getDefaultConfiguration()
        .getBounds();
  }

  @Override
  public String toString() {
    return getName();
  }
}
