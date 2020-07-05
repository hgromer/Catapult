package com.catapult.managers;

import com.catapult.monitor.Monitor;
import com.catapult.monitor.MonitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class MacOsManager implements OsManager {
  private static final Logger LOG = LoggerFactory.getLogger(MacOsManager.class);
  private static final Runtime RUNTIME = Runtime.getRuntime();
  private static final String FOREGROUND_FILE = "foreground.txt";

  @Override
  public String getForegroundApplication() throws IOException {
    String command = getResourceFileAsString(FOREGROUND_FILE);
    Process process = runAppleScriptCommand(command);
    return getProcessOutput(process);
  }

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
  public void moveApplicationToMonitor(String application, int monitorNumber) {
    Optional<Monitor> monitorMaybe = MonitorFactory.getMonitor(monitorNumber);
    if (monitorMaybe.isPresent()) {
      Rectangle rectangle = monitorMaybe.get().getBounds();
      try {
        String command = getMoveCommandForApplication(application, rectangle.x, rectangle.y);
        runAppleScriptCommand(command);
      } catch (Exception e) {
        LOG.error("Unable to move application {}", application, e);
      }
    }
  }

  private String getResourceFileAsString(String filename) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(filename)) {
      if (is == null) return null;
      try (InputStreamReader isr = new InputStreamReader(is);
           BufferedReader reader = new BufferedReader(isr)) {
        String file = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        LOG.info("Loaded file contents {}", file);
        return file;
      }
    }
  }

  private String getProcessOutput(Process process) throws IOException {
    try (InputStream is = process.getInputStream()) {
      String result = new String(is.readAllBytes());
      LOG.info("Retrieved process result {}", result);
      return result.trim();
    }
  }

  private Process runAppleScriptCommand(String command) throws IOException {
    String[] args = {"osascript", "-e", command};
    LOG.info("Running command {}", command);
    return RUNTIME.exec(args);
  }

  private String getMoveCommandForApplication(String application, int x, int y) {
    return String.format("tell application \"System Events\"\n" +
        "    set position of first window of application process \"%s\" to {%d, %d}\n" +
        "end tell", application, x, y);
  }
}
