package jstester;

final class StackTraceProperties {

  private final JsFileProperties testCode;
  private final JsFileProperties testUtil;
  private final String srcName;

  StackTraceProperties(JsFileProperties testCodeProps,
      JsFileProperties testUtilProps, String sourceName) {
    testCode = testCodeProps;
    testUtil = testUtilProps;
    srcName = sourceName;
  }

  JsFileProperties getTestCode() {
    return testCode;
  }

  JsFileProperties getTestUtil() {
    return testUtil;
  }

  String getSrcName() {
    return srcName;
  }
}
