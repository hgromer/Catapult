package com.catapult;

import com.catapult.listener.KeyListener;
import com.catapult.listener.QuitListener;
import org.jnativehook.GlobalScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
  public static void main(String[] args) throws Exception {
    // Turn off JNativeHook logger because it's noisy
    Logger.getLogger(GlobalScreen.class.getPackage().getName())
        .setLevel(Level.OFF);
    if (!Platform.isMac()) {
      throw new IllegalStateException("Mac is the only platform currently supported");
    }
    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(new QuitListener());
    GlobalScreen.addNativeKeyListener(new KeyListener());
  }
}
