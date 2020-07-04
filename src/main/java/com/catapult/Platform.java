package com.catapult;

public class Platform {
  private static final String OS = System.getProperty("os.name");

  public static boolean isMac() {
    return OS.startsWith("Mac");
  }
}
