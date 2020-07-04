package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitListener extends AbstractKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(KeyListener.class);

  public QuitListener() {
    super(
        new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_Q, true)
    );
  }

  @Override
  protected void onAllPressed() {
    try {
      LOG.info("Gracefully shutting down Catapult...");
      GlobalScreenManager.unregisterNativeHook();
      LOG.info("Catapult shutdown");
    } catch (NativeHookException e) {
      LOG.error("Error quiting...could not unregister native hook", e);
    }
  }

  @Override
  protected void onReleased() {

  }
}
