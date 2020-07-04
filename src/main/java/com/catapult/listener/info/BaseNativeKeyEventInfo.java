package com.catapult.listener.info;

import java.util.Set;

public interface BaseNativeKeyEventInfo {
  boolean isActivationKey();
  Set<Integer> getMatchingKeyEventCodes();
}
