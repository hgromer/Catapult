package com.catapult.listener;

import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitListener implements NativeKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(KeyListener.class);

  private short hotKeyFlag = 0x000;
  private final short MASK_CTR = 1 << 0;
  private final short MASK_SH = 1 << 1;
  private final short MASK_Q = 1 << 10;

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

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_Q) {
      hotKeyFlag &= MASK_Q;
      if (hotKeyFlag == (MASK_CTR & MASK_SH & MASK_Q)) {
        try {
          LOG.info("Gracefully shutting down Catapult...");
          GlobalScreenManager.unregisterNativeHook();
          LOG.info("Catapult shutdown");
        } catch (NativeHookException e) {
          LOG.error("Error quiting...could not unregister native hook", e);
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

    if (nativeKeyEvent.getKeyCode() == NativeKeyEvent.VC_Q) {
      hotKeyFlag ^= MASK_Q;
    }

  }
}
