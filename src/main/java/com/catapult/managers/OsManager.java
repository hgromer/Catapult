package com.catapult.managers;

import com.catapult.monitor.Monitor;

import java.io.IOException;
import java.util.Optional;

public interface OsManager {
  String getForegroundApplication() throws IOException;
  Optional<Monitor> getMonitorWithMouse();
  void moveApplicationToMonitor(String application, int monitorNumber);
}
