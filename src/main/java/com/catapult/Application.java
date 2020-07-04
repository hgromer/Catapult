package com.catapult;

import com.catapult.listener.DisplayMonitorInfoListener;
import com.catapult.listener.GlobalScreenManager;
import com.catapult.listener.KeyListener;
import com.catapult.listener.QuitListener;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
  private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);
  public static void main(String[] args) {
    // Turn off JNativeHook logger because it's noisy
    Logger.getLogger(GlobalScreen.class.getPackage().getName())
        .setLevel(Level.OFF);
    if (!Platform.isMac()) {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
    try {
      GlobalScreenManager.registerNativeHook();
      GlobalScreenManager.addNativeKeyListener(new QuitListener());
      GlobalScreenManager.addNativeKeyListener(new DisplayMonitorInfoListener());
      GlobalScreenManager.addNativeKeyListener(new KeyListener());
    } catch (Exception e) {
      try {
        LOG.error("Caught exception while trying to launch Catapult, attempting graceful shutdown...", e);
        GlobalScreenManager.unregisterNativeHook();
      } catch (NativeHookException ne) {
        LOG.error("Unable to unregister native hooks", ne);
      }
    }
  }
}
