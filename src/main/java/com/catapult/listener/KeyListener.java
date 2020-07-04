package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import com.catapult.listener.info.NumberNativeKeyEventInfo;
import com.catapult.mac.MacOsManager;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KeyListener extends AbstractKeyListener  {
  private static final Logger LOG = LoggerFactory.getLogger(KeyListener.class);

  private NativeKeyEvent currentKeyEvent;

  public KeyListener() {
    super(
        new NativeKeyEventInfo((short) (1 << 0), NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo((short) (1 << 1), NativeKeyEvent.VC_SHIFT, false),
        new NumberNativeKeyEventInfo((short) (1 << 10), true)
    );
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    currentKeyEvent = nativeKeyEvent;
    super.nativeKeyPressed(nativeKeyEvent);
  }

  @Override
  protected void onAllPressed() {
    try {
      int monitorNumber = Integer.parseInt(NativeKeyEvent.getKeyText(currentKeyEvent.getKeyCode()));
      String application = MacOsManager.getForegroundApplication();
      LOG.info("Moving {} to monitor {}", application, monitorNumber - 1);
      MacOsManager.moveApplicationToMonitor(application, monitorNumber);
    } catch (IOException e) {
      LOG.error("Could not move application", e);
    }
  }

  @Override
  protected void onReleased() {

  }
}
