package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import com.catapult.monitor.MonitorFactory;
import com.catapult.monitor.MonitorInfoDisplay;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayMonitorInfoListener extends AbstractKeyListener implements AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(DisplayMonitorInfoListener.class);

  private final List<MonitorInfoDisplay> monitorInfoDisplays;

  public DisplayMonitorInfoListener() {
    super(
        new NativeKeyEventInfo((short) (1 << 0), NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo((short) (1 << 1), NativeKeyEvent.VC_SHIFT, false),
        new NativeKeyEventInfo((short) (1 << 10), NativeKeyEvent.VC_M, true)
    );

    this.monitorInfoDisplays = MonitorFactory.getMonitors().stream()
        .map(MonitorInfoDisplay::new)
        .collect(Collectors.toList());
  }


  @Override
  public void close() throws Exception {
    for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
      LOG.info("Killing monitor info display {}", monitorInfoDisplay);
      monitorInfoDisplay.kill();
    }
  }

  @Override
  protected void onAllPressed() {
    for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
      monitorInfoDisplay.display();
    }
  }

  @Override
  protected void onReleased() {
    for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
      monitorInfoDisplay.hide();
    }
  }
}
