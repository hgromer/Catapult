package com.catapult.managers;

import com.catapult.monitor.Monitor;
import com.catapult.monitor.MonitorFactory;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Optional;

public class WindowsOsManager implements OsManager {
  private static final Logger LOG = LoggerFactory.getLogger(WindowsOsManager.class);

  @Override
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

  @Override
  public void moveForegroundApplicationToMonitor(int monitorNumber) {
    Optional<Monitor> monitorMaybe = MonitorFactory.getMonitor(monitorNumber);
    if (monitorMaybe.isPresent()) {
      WinDef.HWND foregroundWindow = User32.INSTANCE.GetForegroundWindow();
      Rectangle bounds = monitorMaybe.get().getBounds();
      WinDef.RECT rect = new WinDef.RECT();
      User32.INSTANCE.GetWindowRect(foregroundWindow, rect);
      Rectangle currentWindowSize = rect.toRectangle();
      boolean result = User32.INSTANCE.MoveWindow(
          foregroundWindow,
          bounds.x,
          bounds.y,
          Math.min(currentWindowSize.width, bounds.width),
          Math.min(currentWindowSize.height, bounds.height),
          false
          );
      if (!result) {
        LOG.error("Failed to move application {}", foregroundWindow);
      }
    }
  }

  @Override
  public void clean() {

  }
}
