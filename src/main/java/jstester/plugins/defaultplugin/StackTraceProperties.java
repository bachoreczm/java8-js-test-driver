package jstester.plugins.defaultplugin;

import jstester.JsFileProperties;

final class StackTraceProperties {

  private final JsFileProperties testUtil;
  private final JsFileProperties[] codes;

  StackTraceProperties(JsFileProperties testUtilProps,
      JsFileProperties... sourceProps) {
    testUtil = testUtilProps;
    codes = sourceProps;
  }

  JsFileProperties getTestUtil() {
    return testUtil;
  }

  JsFileProperties getCodeByIndex(int i) {
    return codes[i];
  }
}
