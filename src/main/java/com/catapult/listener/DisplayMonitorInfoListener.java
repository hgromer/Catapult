package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import com.catapult.monitor.MonitorFactory;
import com.catapult.gui.MonitorInfoDisplay;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayMonitorInfoListener extends AbstractKeyListener implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(DisplayMonitorInfoListener.class);

  private List<MonitorInfoDisplay> currentMonitorInfoDisplays;

  public DisplayMonitorInfoListener() {
    super(
        new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_M, true)
    );
    this.currentMonitorInfoDisplays = getCurrentMonitorInfoDisplays();
  }


  @Override
  public synchronized void close() {
    for (MonitorInfoDisplay monitorInfoDisplay : currentMonitorInfoDisplays) {
      LOG.info("Killing monitor info display {}", monitorInfoDisplay);
      monitorInfoDisplay.kill();
    }
  }

  @Override
  protected String getVisibleName() {
    return "Display monitor numbers";
  }

  @Override
  protected synchronized void onAllPressed() {
    this.currentMonitorInfoDisplays = getCurrentMonitorInfoDisplays();
    for (MonitorInfoDisplay display : currentMonitorInfoDisplays) {
      display.display();
    }
  }

  @Override
  protected synchronized void onReleased() {
    for (MonitorInfoDisplay display : currentMonitorInfoDisplays) {
      display.hide();
    }
  }

  private List<MonitorInfoDisplay> getCurrentMonitorInfoDisplays() {
    return MonitorFactory.getMonitors().stream()
        .map(MonitorInfoDisplay::new)
        .collect(Collectors.toUnmodifiableList());
  }
}
