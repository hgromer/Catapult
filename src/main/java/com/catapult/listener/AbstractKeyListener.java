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

  private short hotKeyFlag = 0x00;
  private short allMasksActive = 0x00;

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
        allMasksActive &= keyEventInfo.getValue();
      }
    }

    nativeKeyEventInfoMap = builder.build();
  }

  @Override
  public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {

  }

  @Override
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

    if (nativeKeyEventInfoMaybe.isPresent()) {
      BaseNativeKeyEventInfo baseNativeKeyEventInfo = nativeKeyEventInfoMaybe.get();
      hotKeyFlag &= baseNativeKeyEventInfo.getValue();

      if (baseNativeKeyEventInfo.isActivationKey()) {
        if (hotKeyFlag == allMasksActive) {
          onAllPressed();
        }
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
    Optional<BaseNativeKeyEventInfo> nativeKeyEventInfoMaybe = Optional.ofNullable(nativeKeyEventInfoMap.get(nativeKeyEvent.getKeyCode()));

    if (nativeKeyEventInfoMaybe.isPresent()) {
      BaseNativeKeyEventInfo baseNativeKeyEventInfo = nativeKeyEventInfoMaybe.get();
      hotKeyFlag ^= baseNativeKeyEventInfo.getValue();
    }

    if (hotKeyFlag != allMasksActive) {
      onReleased();
    }

  }

  protected abstract void onAllPressed();
  protected abstract void onReleased();
}
