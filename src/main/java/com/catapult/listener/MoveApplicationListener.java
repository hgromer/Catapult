package com.catapult.listener;

import com.catapult.listener.info.NativeKeyEventInfo;
import com.catapult.listener.info.NumberNativeKeyEventInfo;
import com.catapult.managers.OsManager;
import com.catapult.managers.OsManagerFactory;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveApplicationListener extends AbstractKeyListener  {
  private static final Logger LOG = LoggerFactory.getLogger(MoveApplicationListener.class);

  private final OsManager osManager;
  private NativeKeyEvent currentKeyEvent;

  public MoveApplicationListener() {
    super(
        new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
        new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
        new NumberNativeKeyEventInfo(true)
    );
    this.osManager = OsManagerFactory.getOsManager();
  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    currentKeyEvent = nativeKeyEvent;
    super.nativeKeyPressed(nativeKeyEvent);
  }

  @Override
  protected String getVisibleName() {
    return "Move application";
  }

  @Override
  protected void onAllPressed() {
    try {
      int monitorNumber = Integer.parseInt(NativeKeyEvent.getKeyText(currentKeyEvent.getKeyCode()));
      LOG.info("Moving application to monitor {}", monitorNumber - 1);
      osManager.moveForegroundApplicationToMonitor(monitorNumber);
    } catch (Exception e) {
      LOG.error("Could not move application", e);
    }
  }

  @Override
  protected void onReleased() {

  }
}
