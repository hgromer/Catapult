package com.catapult.mac;

import com.catapult.monitor.Monitor;
import com.catapult.monitor.MonitorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class MacOsManager {
  private static final Logger LOG = LoggerFactory.getLogger(MacOsManager.class);
  private static final Runtime RUNTIME = Runtime.getRuntime();
  private static final String FOREGROUND_FILE = "foreground.txt";

  public static String getForegroundApplication() throws IOException {
    String command = getResourceFileAsString(FOREGROUND_FILE);
    try (InputStream is = runAppleScriptCommand(command).getInputStream()) {
      String result = new String(is.readAllBytes());
      LOG.info("Retrieved result {} from getForegroundApplication", result);
      return result.trim();
    }
  }

  public static void moveApplicationToMonitor(String app, int monitorNumber) {
    Optional<Monitor> monitorMaybe = MonitorFactory.getMonitor(monitorNumber);
    if (monitorMaybe.isPresent()) {
      Rectangle rectangle = monitorMaybe.get().getBounds();
      try {
        String command = getMoveCommandForApplication(app, rectangle.x, rectangle.y);
        runAppleScriptCommand(command);
      } catch (Exception e) {
        LOG.error("Unable to move application {}", app, e);
      }
    }
  }

  private static String getResourceFileAsString(String filename) throws IOException {
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

  private static Process runAppleScriptCommand(String command) throws IOException {
    String[] args = {"osascript", "-e", command};
    LOG.info("Running command {}", command);
    return RUNTIME.exec(args);
  }

  private static String getMoveCommandForApplication(String application, int x, int y) {
    return String.format("tell application \"System Events\"\n" +
        "    set position of first window of application process \"%s\" to {%d, %d}\n" +
        "end tell", application, x, y);
  }
}
