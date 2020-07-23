package com.catapult.managers;

import com.catapult.listener.GlobalScreenManager;
import com.catapult.monitor.Monitor;
import com.catapult.monitor.MonitorFactory;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public abstract class OsManager {
  private static final Logger LOG = LoggerFactory.getLogger(OsManager.class);

  public abstract void moveForegroundApplicationToMonitor(int monitorNumber) throws Exception;

  public Optional<Monitor> getMonitorWithMouse() {
    Collection<Monitor> monitors = MonitorFactory.getMonitors();
    GraphicsDevice deviceWithMouse = MouseInfo.getPointerInfo().getDevice();
    for (Monitor monitor : monitors) {
      GraphicsDevice device = monitor.getConfiguration().getDevice();
      if (device.getIDstring().equals(deviceWithMouse.getIDstring())) {
        LOG.info("Mouse at monitor {}", monitor.getDisplayableMonitorNumber());
        return Optional.of(monitor);
      }
    }
    LOG.warn("Could not find monitor where mouse is located, defaulting to first monitor");
    return MonitorFactory.getMonitor(1);
  }

  public void clean() {
    GlobalScreenManager.unregisterNativeHook();
  }
}
