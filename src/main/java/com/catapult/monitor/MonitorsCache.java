package com.catapult.monitor;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonitorsCache implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(MonitorsCache.class);
  private static final int UPDATE_SLEEP_SECONDS = 2;
  private static final GraphicsEnvironment GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();
  private static final Object LOCK = new Object();

  private final Thread updateThread;
  private final AtomicBoolean alive;

  private Map<Integer, Monitor> monitors;

  public MonitorsCache() {
    this.monitors = getCurrentMonitors();
    this.alive = new AtomicBoolean(true);
    this.updateThread = new Thread(this::update);

    this.updateThread.start();
  }

  public Collection<Monitor> getMonitors() {
    synchronized (LOCK) {
      return monitors.values();
    }
  }

  public Optional<Monitor> getMonitorByIndex(int index) {
    synchronized (LOCK) {
      return Optional.ofNullable(monitors.get(index));
    }
  }

  @Override
  public void close() throws Exception {
    LOG.info("Terminating update thread...");
    alive.set(false);
    updateThread.join();
    LOG.info("Update thread terminated");
  }

  private Map<Integer, Monitor> getCurrentMonitors() {
    ImmutableMap.Builder<Integer, Monitor> builder = ImmutableMap.builder();
    GraphicsDevice[] devices = GRAPHICS_ENVIRONMENT.getScreenDevices();

    for (int i = 0; i < devices.length; ++i) {
      builder.put(i, new Monitor(devices[i], i));
    }
    return builder.build();
  }

  private void update() {
    LOG.info("Launching monitor update thread");
    while (alive.get()) {
      try {
        Thread.sleep(UPDATE_SLEEP_SECONDS * 1000);
      } catch (InterruptedException e) {
        LOG.error("Could not sleep in update thread", e);
      }
      synchronized (LOCK) {
        this.monitors = getCurrentMonitors();
      }
    }
  }
}
