package com.catapult.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Optional;

public class MonitorFactory {
  private static final Logger LOG = LoggerFactory.getLogger(MonitorFactory.class);
  private static final MonitorsCache MONITORS_CACHE = new MonitorsCache();

  public static Collection<Monitor> getMonitors() {
    return MONITORS_CACHE.getMonitors();
  }

  public static Optional<Monitor> getMonitor(int monitorNumber) {
    --monitorNumber;
    Optional<Monitor> result = MONITORS_CACHE.getMonitorByIndex(monitorNumber);
    if (result.isEmpty()) {
      LOG.warn("Invalid monitor {}", monitorNumber);
    }
    return result;
  }

  public static void close() {
    try {
      MONITORS_CACHE.close();
    } catch (Exception e) {
      LOG.error("Failed to close monitor cache", e);
    }
  }
}
