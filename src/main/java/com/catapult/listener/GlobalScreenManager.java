package com.catapult.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GlobalScreenManager {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalScreenManager.class);
  private static final Set<AbstractKeyListener> LISTENERS = new HashSet<>();
  private static final EventConsumerDispatcher DISPATCH_SERVICE = new EventConsumerDispatcher();

  public static void registerNativeHook() throws NativeHookException {
    GlobalScreen.setEventDispatcher(DISPATCH_SERVICE);
    GlobalScreen.registerNativeHook();
  }

  public static void unregisterNativeHook() {
    for (AbstractKeyListener listener : LISTENERS) {
      LOG.info("Removing listener {}...", listener);
      GlobalScreen.removeNativeKeyListener(listener);
      LOG.info("Listener {} removed", listener);
    }
    // Has to occur in a separate thread or else the program
    // will block here and won't terminate gracefully
    EventQueue.invokeLater(() -> {
      try {
        LOG.info("Unregistering native hooks...");
        GlobalScreen.unregisterNativeHook();
        LOG.info("Native hooks unregistered");
      } catch (NativeHookException e) {
        LOG.error("Could not unregister native hooks", e);
      }
    });
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
