package com.catapult.listener;

import com.catapult.gui.GuiManager;
import com.catapult.gui.OptionsGui;
import com.catapult.listener.info.NativeKeyEventInfo;
import org.jnativehook.keyboard.NativeKeyEvent;

public class GuiActivateListener extends AbstractKeyListener implements AutoCloseable {
  private final OptionsGui optionsGui = GuiManager.getOptionsGui();

  public GuiActivateListener() {
    super(
      new NativeKeyEventInfo(NativeKeyEvent.VC_CONTROL, false),
      new NativeKeyEventInfo(NativeKeyEvent.VC_SHIFT, false),
      new NativeKeyEventInfo(NativeKeyEvent.VC_G, true)
    );
  }

  @Override
  protected void onAllPressed() {
    optionsGui.show();
  }

  @Override
  protected void onReleased() {

  }

  @Override
  public void close() throws Exception {
    optionsGui.close();
  }
}
