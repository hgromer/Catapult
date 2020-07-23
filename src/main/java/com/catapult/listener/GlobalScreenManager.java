package com.catapult.listener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class GlobalScreenManager {
  private static final Logger LOG = LoggerFactory.getLogger(GlobalScreenManager.class);
  private static final Set<AbstractKeyListener> LISTENERS = new HashSet<>();

  private static class VoidDispatchService extends AbstractExecutorService {
    private boolean running = false;

    public VoidDispatchService() {
      running = true;
    }

    public void shutdown() {
      running = false;
    }

    public List<Runnable> shutdownNow() {
      running = false;
      return new ArrayList<Runnable>(0);
    }

    public boolean isShutdown() {
      return !running;
    }

    public boolean isTerminated() {
      return !running;
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
      return true;
    }

    public void execute(Runnable r) {
      r.run();
    }
  }

  public static void registerNativeHook() throws NativeHookException {
    GlobalScreen.setEventDispatcher(new VoidDispatchService());
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
