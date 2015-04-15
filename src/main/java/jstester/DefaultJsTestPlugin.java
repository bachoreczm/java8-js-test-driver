package jstester;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.computeUserCode;
import static jstester.JsTester.getCode;
import static jstester.JsTester.newEngine;
import static jstester.StackTraceFormatter.formattingStackTraces;

import java.io.IOException;

import javax.script.ScriptException;

public class DefaultJsTestPlugin implements JsTestPlugin {

  private String lastStackTraces;

  @Override
  public void eval(JsFileProperties[] userCodes) throws IOException,
      ScriptException {
    if (userCodes.length == 0) {
      return;
    }
    final String userCode = computeUserCode(userCodes);
    final JsFileProperties testUtil = getCode(JS_TEST_UTIL);
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String code = computeCode(testUtil, userCode, runningTests);
    String stackTraces = (String) newEngine().eval(code);
    StackTraceProperties stackProps = new StackTraceProperties(testUtil,
        userCodes);
    lastStackTraces = formattingStackTraces(stackTraces, stackProps);
  }

  /**
   * @return the last {@link #eval(JsFileProperties[])} result.
   */
  public String getLastStackTraces() {
    return lastStackTraces;
  }

  private static String computeCode(JsFileProperties testUtil, String userCode,
      String runningTests) {
    return testUtil + "\n" + userCode + "\n" + runningTests;
  }
}
