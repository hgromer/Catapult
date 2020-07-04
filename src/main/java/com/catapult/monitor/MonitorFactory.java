package com.catapult.monitor;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class MonitorFactory {
  private static final Logger LOG = LoggerFactory.getLogger(MonitorFactory.class);
  private static final Map<Integer, Monitor> MONITORS;

  static {
    GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getScreenDevices();
    ImmutableMap.Builder<Integer, Monitor> builder = ImmutableMap.builder();

    for (int i = 0; i < devices.length; ++i) {
      builder.put(i, new Monitor(devices[i], i));
    }
    MONITORS = builder.build();
  }

  public static Collection<Monitor> getMonitors() {
    return MONITORS.values();
  }

  public static Optional<Monitor> getMonitor(int monitorNumber) {
    --monitorNumber;
    Optional<Monitor> result = Optional.ofNullable(MONITORS.get(monitorNumber));
    if (result.isEmpty()) {
      LOG.warn("Invalid monitor {}", monitorNumber);
    }
    return result;
  }
}
