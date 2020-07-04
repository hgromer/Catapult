package com.catapult.listener;

import com.catapult.monitor.MonitorFactory;
import com.catapult.monitor.MonitorInfoDisplay;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DisplayMonitorInfoListener implements NativeKeyListener, AutoCloseable {
  private static final Logger LOG = LoggerFactory.getLogger(DisplayMonitorInfoListener.class);

  private short hotKeyFlag = 0x000;
  private final short MASK_CTR = 1 << 0;
  private final short MASK_SH = 1 << 1;
  private final short MASK_M = 1 << 10;

  private final List<MonitorInfoDisplay> monitorInfoDisplays;

  public DisplayMonitorInfoListener() {
    super();

    this.monitorInfoDisplays = MonitorFactory.getMonitors().stream()
        .map(MonitorInfoDisplay::new)
        .collect(Collectors.toList());
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
      hotKeyFlag &= MASK_CTR;
    }

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
      hotKeyFlag &= MASK_SH;
    }

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_M) {
      hotKeyFlag &= MASK_M;
      if (hotKeyFlag == (MASK_CTR & MASK_SH & MASK_M)) {
        for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
          monitorInfoDisplay.display();
        }
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_CONTROL) {
      hotKeyFlag ^= MASK_CTR;
    }

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_SHIFT) {
      hotKeyFlag ^= MASK_SH;
    }

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_M) {
      hotKeyFlag ^= MASK_M;
    }

    if (hotKeyFlag != (MASK_CTR & MASK_SH & MASK_M)) {
      for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
        monitorInfoDisplay.hide();
      }
    }
  }

  @Override
  public void close() throws Exception {
    for (MonitorInfoDisplay monitorInfoDisplay : monitorInfoDisplays) {
      LOG.info("Killing monitor info display {}", monitorInfoDisplay);
      monitorInfoDisplay.kill();
    }
  }
}
