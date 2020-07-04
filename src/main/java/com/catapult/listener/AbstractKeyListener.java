package com.catapult.listener;

import com.catapult.listener.info.BaseNativeKeyEventInfo;
import com.google.common.collect.ImmutableMap;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractKeyListener implements NativeKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractKeyListener.class);

  private final Map<Integer, BaseNativeKeyEventInfo> nativeKeyEventInfoMap;
  private final int totalPossibleActiveKeys;

  private AtomicInteger keysActive = new AtomicInteger(0);

  AbstractKeyListener(BaseNativeKeyEventInfo... keyEventInfos) {
    ImmutableMap.Builder<Integer, BaseNativeKeyEventInfo> builder = ImmutableMap.builder();

    boolean activationFound = false;
    Set<Integer> keyCodesSeen = new HashSet<>();
    int maskCum = 0x00;

    for (BaseNativeKeyEventInfo keyEventInfo : keyEventInfos) {
      if (keyEventInfo.isActivationKey()) {
        if (activationFound) {
          throw new IllegalStateException("Cannot have multiple activation keys");
        }
        activationFound = true;
      }
      for (int keyCode : keyEventInfo.getMatchingKeyEventCodes()) {
        if (keyCodesSeen.contains(keyCode)) {
          throw new IllegalStateException("Cannot have multiple bindings for key code" + keyCode);
        }
        builder.put(keyCode, keyEventInfo);
        keyCodesSeen.add(keyCode);
      }
    }

    this.nativeKeyEventInfoMap = builder.build();
    this.totalPossibleActiveKeys = keyEventInfos.length;
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (keysActive.get() < totalPossibleActiveKeys) {
      LOG.info("Current active keys {}", keysActive.get());
      Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

      if (nativeKeyEventInfoMaybe.isPresent()) {
        BaseNativeKeyEventInfo baseNativeKeyEventInfo = nativeKeyEventInfoMaybe.get();
        int currentActive = keysActive.addAndGet(1);

        if (baseNativeKeyEventInfo.isActivationKey()) {
          if (totalPossibleActiveKeys == currentActive) {
            onAllPressed();
          }
        }
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

    if (nativeKeyEventInfoMaybe.isPresent()) {
      keysActive.decrementAndGet();
      onReleased();
    }

  }

  protected abstract void onAllPressed();
  protected abstract void onReleased();
}
