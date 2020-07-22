package com.catapult.managers;

import com.catapult.monitor.Monitor;

import java.util.Optional;

public interface OsManager {
  Optional<Monitor> getMonitorWithMouse();
  void moveForegroundApplicationToMonitor(int monitorNumber) throws Exception;
  void clean();
}
