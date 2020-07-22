package com.catapult;

import com.catapult.listener.DisplayMonitorInfoListener;
import com.catapult.listener.GlobalScreenManager;
import com.catapult.listener.GuiActivateListener;
import com.catapult.listener.MoveApplicationListener;
import com.catapult.listener.QuitListener;
import com.catapult.managers.OsManager;
import com.catapult.managers.OsManagerFactory;
import org.jnativehook.GlobalScreen;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);
  public static void main(String[] args) {
    OsManager osManager = OsManagerFactory.getOsManager();
    // Turn off JNativeHook logger because it's noisy
    Logger.getLogger(GlobalScreen.class.getPackage().getName()).setLevel(Level.WARNING);
    try {
      GlobalScreenManager.registerNativeHook();
      GlobalScreenManager.addNativeKeyListener(new QuitListener());
      GlobalScreenManager.addNativeKeyListener(new DisplayMonitorInfoListener());
      GlobalScreenManager.addNativeKeyListener(new MoveApplicationListener());
      GlobalScreenManager.addNativeKeyListener(new GuiActivateListener());
    } catch (Exception e) {
      LOG.error("Caught exception while trying to launch Catapult, attempting graceful shutdown...", e);
      osManager.clean();
    }
  }
}
