package jstester.plugins.defaultplugin;

import jstester.JsFile;

final class StackTraceProperties {

  private final JsFile testUtil;
  private final JsFile[] codes;

  StackTraceProperties(JsFile testUtilProps, JsFile... sourceProps) {
    testUtil = testUtilProps;
    codes = sourceProps;
  }

  JsFile getTestUtil() {
    return testUtil;
  }

  JsFile getCodeByIndex(int i) {
    return codes[i];
  }
}
