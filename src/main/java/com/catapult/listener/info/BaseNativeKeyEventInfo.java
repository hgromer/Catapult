package com.catapult.listener.info;

import java.util.Set;

public interface BaseNativeKeyEventInfo {
  short getValue();
  boolean isActivationKey();
  Set<Integer> getMatchingKeyEventCodes();
}
