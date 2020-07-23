package com.catapult.listener;

import com.catapult.listener.info.BaseNativeKeyEventInfo;
import com.google.common.collect.ImmutableMap;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractKeyListener implements NativeKeyListener {
  private static final Logger LOG = LoggerFactory.getLogger(AbstractKeyListener.class);

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
  public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
    Optional<Boolean> isActivationMaybe = Optional.ofNullable(isActivationForEventId.get(nativeKeyEvent.getKeyCode()));

    // If someone leaves the activation key pressed down it will continue to fire
    // this event. So we need to continue to suppress it even after we've reached/exceeded
    // the # of possible active keys
    if (activeKeys.size() >= totalPossibleActiveKeys
        && isActivationMaybe.isPresent()
        && isActivationMaybe.get()) {
      consumeKeyEvent(nativeKeyEvent);
    }

    if (activeKeys.size() < totalPossibleActiveKeys) {

      if (isActivationMaybe.isPresent()) {
        activeKeys.add(nativeKeyEvent.getKeyCode());
        boolean isActivation = isActivationMaybe.get();
        int currentActive = activeKeys.size();

        if (isActivation) {
          if (totalPossibleActiveKeys == currentActive) {
            consumeKeyEvent(nativeKeyEvent);
            onAllPressed();
          }
        }
      }
    }
  }

  @Override
  public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
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

  private void consumeKeyEvent(NativeKeyEvent event) {
    try {
      Field field = NativeInputEvent.class.getDeclaredField("reserved");
      field.setAccessible(true);
      field.setShort(event, (short) 0x01);
    } catch (Exception e) {
      LOG.error("Failed to consume native key event {}", event, e);
    }
  }
}

