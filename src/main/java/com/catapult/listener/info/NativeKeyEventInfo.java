package com.catapult.listener.info;

import java.util.Collections;
import java.util.Set;

public class NativeKeyEventInfo implements BaseNativeKeyEventInfo {
  private final Set<Integer> keyEventCode;
  private final boolean isActivation;

  public NativeKeyEventInfo(int keyEventCode, boolean isActivation) {
    this.keyEventCode = Collections.singleton(keyEventCode);
    this.isActivation = isActivation;
  }

  @Override
  public boolean isActivationKey() {
    return isActivation;
  }

  @Override
  public Set<Integer> getMatchingKeyEventCodes() {
    return keyEventCode;
  }
}
