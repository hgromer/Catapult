package com.catapult.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class GlobalScreenManager {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalScreenManager.class);
  private static final Set<AutoCloseable> closeables = new HashSet<>();

  public static void registerNativeHook() throws NativeHookException {
    GlobalScreen.registerNativeHook();
  }

  public static void unregisterNativeHook() throws NativeHookException {
    for (AutoCloseable closeable : closeables) {
      try {
        LOG.info("Attempting to close {}...", closeable);
        closeable.close();
        LOG.info("{} closed", closeable);
      } catch (Exception e) {
        LOG.error("Failed to close auto closeable", e);
      }
    }
    GlobalScreen.unregisterNativeHook();
  }

  public static void addNativeKeyListener(NativeKeyListener nativeKeyListener) {
    GlobalScreen.addNativeKeyListener(nativeKeyListener);
    if (nativeKeyListener instanceof AutoCloseable) {
      LOG.info("Registering {} as autocloseable", nativeKeyListener.getClass());
      closeables.add((AutoCloseable) nativeKeyListener);
    }
  }

}
