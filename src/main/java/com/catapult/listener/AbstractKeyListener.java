package com.catapult.listener;

import com.catapult.listener.info.BaseNativeKeyEventInfo;
import com.google.common.collect.ImmutableMap;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractKeyListener implements NativeKeyListener {
  private final Map<Integer, BaseNativeKeyEventInfo> nativeKeyEventInfoMap;
  private final int totalPossibleActiveKeys;

  private final Set<Integer> activeKeys;

  AbstractKeyListener(BaseNativeKeyEventInfo... keyEventInfos) {
    ImmutableMap.Builder<Integer, BaseNativeKeyEventInfo> builder = ImmutableMap.builder();

    boolean activationFound = false;
    Set<Integer> keyCodesSeen = new HashSet<>();

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
    this.activeKeys = new HashSet<>();
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public synchronized void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (activeKeys.size() < totalPossibleActiveKeys) {
      Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

      if (nativeKeyEventInfoMaybe.isPresent()) {
        activeKeys.add(nativeKeyEvent.getKeyCode());
        BaseNativeKeyEventInfo baseNativeKeyEventInfo = nativeKeyEventInfoMaybe.get();
        int currentActive = activeKeys.size();

        if (baseNativeKeyEventInfo.isActivationKey()) {
          if (totalPossibleActiveKeys == currentActive) {
            onAllPressed();
          }
        }
      }
    }
  }

  @Override
  public synchronized void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

    if (nativeKeyEventInfoMaybe.isPresent()) {
      activeKeys.remove(nativeKeyEvent.getKeyCode());
      onReleased();
    }
  }

  protected abstract void onAllPressed();
  protected abstract void onReleased();
}
