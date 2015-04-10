package jstester;

final class StackTraceProperties {

  private final JsFileProperties testCode;
  private final JsFileProperties testUtil;
  private final JsFileProperties[] srcCodes;

  StackTraceProperties(JsFileProperties testCodeProps,
      JsFileProperties testUtilProps, JsFileProperties... sourceProps) {
    testCode = testCodeProps;
    testUtil = testUtilProps;
    srcCodes = sourceProps;
  }

  JsFileProperties getTestCode() {
    return testCode;
  }

  JsFileProperties getTestUtil() {
    return testUtil;
  }

  JsFileProperties getCodeByIndex(int i) {
    if (i == 0) {
      return testCode;
    } else {
      return srcCodes[i - 1];
    }
  }
}
