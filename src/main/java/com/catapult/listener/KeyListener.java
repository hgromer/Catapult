package com.catapult.listener;

import com.catapult.mac.MacOsManager;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class KeyListener implements NativeKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(KeyListener.class);

  private short hotKeyFlag = 0x000;
  private static final short MASK_CTR = 1 << 0;
  private static final short MASK_SH = 1 << 1;
  private static final short MASK_NUM = 1 << 10;

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

    if (isNumber(nativeKeyEvent)) {
      hotKeyFlag &= MASK_NUM;

      if (hotKeyFlag == (MASK_CTR & MASK_SH & MASK_NUM)) {

        try {
          int monitorNumber = Integer.parseInt(NativeKeyEvent.getKeyText(nativeKeyEvent.getKeyCode()));
          String application = MacOsManager.getForegroundApplication();
          LOG.info("Moving {} to monitor {}", application, monitorNumber - 1);
          MacOsManager.moveApplicationToMonitor(application, monitorNumber);
        } catch (IOException e) {
          LOG.error("Could not move application", e);
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

    if (isNumber(nativeKeyEvent)) {
      hotKeyFlag ^= MASK_NUM;
    }
  }

  private boolean isNumber(NativeKeyEvent nativeKeyEvent) {
    /**
     * VC_1 = key code 2
     * VC_0 = key code 0
     */
    for (int i = 2; i < 12; ++i) {
      if (nativeKeyEvent.getKeyCode() == i) {
        return true;
      }
    }
    return false;
  }
}
