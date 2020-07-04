package com.catapult;

import com.catapult.listener.KeyListener;
import org.jnativehook.GlobalScreen;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
  public static void main(String[] args) throws Exception {
    Logger.getLogger(GlobalScreen.class.getPackage().getName())
        .setLevel(Level.OFF);
    if (!Platform.isMac()) {
      throw new IllegalStateException("Platform must be mac");
    }
    GlobalScreen.registerNativeHook();
    GlobalScreen.addNativeKeyListener(new KeyListener());
  }
}
