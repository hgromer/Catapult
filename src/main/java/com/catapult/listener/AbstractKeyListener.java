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
  private final Map<Integer, Boolean> isActivationForEventId;
  private final int totalPossibleActiveKeys;

  private final Set<Integer> activeKeys;

  AbstractKeyListener(BaseNativeKeyEventInfo... keyEventInfos) {
    ImmutableMap.Builder<Integer, Boolean> builder = ImmutableMap.builder();

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
        builder.put(keyCode, keyEventInfo.isActivationKey());
        keyCodesSeen.add(keyCode);
      }
    }

    this.isActivationForEventId = builder.build();
    this.totalPossibleActiveKeys = keyEventInfos.length;
    this.activeKeys = new HashSet<>();
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public synchronized void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    if (activeKeys.size() < totalPossibleActiveKeys) {
      Optional<Boolean> isActivationMaybe = Optional.ofNullable(isActivationForEventId.get(nativeKeyEvent.getKeyCode()));

      if (isActivationMaybe.isPresent()) {
        activeKeys.add(nativeKeyEvent.getKeyCode());
        boolean isActivation = isActivationMaybe.get();
        int currentActive = activeKeys.size();

        if (isActivation) {
          if (totalPossibleActiveKeys == currentActive) {
            onAllPressed();
          }
        }
      }
    }
  }

  @Override
  public synchronized void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    Optional<Boolean> isActivation = Optional.ofNullable(isActivationForEventId.get(nativeKeyEvent.getKeyCode()));

    if (isActivation.isPresent()) {
      activeKeys.remove(nativeKeyEvent.getKeyCode());
      onReleased();
    }
  }

  @Override
  public String toString() {
    return getVisibleName();
  }

  public ImmutableMap<Integer, Boolean> getIsActivationForEventId() {
    return (ImmutableMap<Integer, Boolean>) isActivationForEventId;
  }

  protected abstract String getVisibleName();
  protected abstract void onAllPressed();
  protected abstract void onReleased();
}
