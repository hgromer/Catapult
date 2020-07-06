package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import com.catapult.monitor.Monitor;
import com.catapult.monitor.MonitorFactory;
import com.catapult.monitor.MonitorInfoDisplay;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DisplayMonitorInfoListener extends AbstractKeyListener implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(DisplayMonitorInfoListener.class);

  private final List<MonitorInfoDisplay> currentMonitorInfoDisplays;

  public DisplayMonitorInfoListener() {
    super(
        new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_M, true)
    );
    this.currentMonitorInfoDisplays = MonitorFactory.getMonitors().stream()
        .map(MonitorInfoDisplay::new)
        .collect(Collectors.toUnmodifiableList());
  }


  @Override
  public void close() {
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
  protected void onAllPressed() {
    for (MonitorInfoDisplay display : currentMonitorInfoDisplays) {
      display.display();
    }
  }

  @Override
  protected void onReleased() {
    for (MonitorInfoDisplay display : currentMonitorInfoDisplays) {
      display.hide();
    }
  }
}
