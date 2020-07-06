package com.catapult.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class GlobalScreenManager {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalScreenManager.class);
  private static final Set<AbstractKeyListener> LISTENERS = new HashSet<>();

  public static void registerNativeHook() throws NativeHookException {
    GlobalScreen.registerNativeHook();
  }

  public static void unregisterNativeHook() {
    LOG.info("Unregistering native hooks...");
    try {
      GlobalScreen.unregisterNativeHook();
      LOG.info("Native hooks unregistered");
    } catch (NativeHookException e) {
      LOG.error("Error unregistering native hooks", e);
    }
  }

  public static void addNativeKeyListener(AbstractKeyListener abstractKeyListener) {
    LOG.info("Adding listener {}", abstractKeyListener);
    GlobalScreen.addNativeKeyListener(abstractKeyListener);
    LISTENERS.add(abstractKeyListener);
  }

  public static Set<AbstractKeyListener> getListeners() {
    return LISTENERS;
  }

}
