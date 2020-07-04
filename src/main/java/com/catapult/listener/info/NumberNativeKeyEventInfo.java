package com.catapult.listener.info;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NumberNativeKeyEventInfo implements BaseNativeKeyEventInfo {
  // VC_1 = 2 | VC_0 = 11
  private static final Set<Integer> MATCHING_KEY_EVENT_CODES = IntStream.range(1, 12).boxed().collect(Collectors.toUnmodifiableSet());

  private final int value;
  private final boolean isActivationKey;

  public NumberNativeKeyEventInfo(int value, boolean isActivationKey) {
    this.value = value;
    this.isActivationKey = isActivationKey;
  }

  @Override
  public boolean isActivationKey() {
    return isActivationKey;
  }

  @Override
  public Set<Integer> getMatchingKeyEventCodes() {
    return MATCHING_KEY_EVENT_CODES;
  }
}
