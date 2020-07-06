package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuitListener extends AbstractKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(MoveApplicationListener.class);

  public QuitListener() {
    super(
        new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_Q, true)
    );
  }

  @Override
  protected String getVisibleName() {
    return "Quit Catapult";
  }

  @Override
  protected void onAllPressed() {
    LOG.info("Gracefully shutting down Catapult...");
    GlobalScreenManager.unregisterNativeHook();
    System.exit(0);
  }

  @Override
  protected void onReleased() {

  }
}
