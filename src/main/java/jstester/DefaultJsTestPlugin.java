package jstester;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.computeUserCode;
import static jstester.JsTester.getCode;
import static jstester.JsTester.newEngine;
import static jstester.SkipFunctionUtil.computeSkipTestFunctions;
import static jstester.StackTraceFormatter.formattingStackTraces;

import java.io.IOException;

import javax.script.ScriptException;

public class DefaultJsTestPlugin implements JsTestPlugin {

  private static final String LOG_START = "JAVA8JSTDLOG:";
  private String lastStackTraces;
  private String lastLogs;

  @Override
  public void eval(JsFileProperties[] userCodes) throws IOException,
      ScriptException {
    if (userCodes.length == 0) {
      return;
    }
    final String userCode = computeUserCode(userCodes);
    final JsFileProperties testUtil = getCode(JS_TEST_UTIL);
    final String skipTests = computeSkipTestFunctions(userCodes);
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String runnerCode = skipTests + runningTests;
    final String code = computeCode(testUtil, userCode, runnerCode);
    String error = (String) newEngine().eval(code);
    String stacktraces = computeLogAndRemoveFromError(error);
    StackTraceProperties stackProps = new StackTraceProperties(testUtil,
        userCodes);
    lastStackTraces = formattingStackTraces(stacktraces, stackProps);
  }

  /**
   * @return the last run's log messages.
   */
  public String getLog() {
    return lastLogs;
  }

  private String computeLogAndRemoveFromError(final String error) {
    StringBuilder errorBuilder = new StringBuilder();
    StringBuilder logBuilder = new StringBuilder();
    for (String row : error.split("\\n")) {
      if (row.startsWith(LOG_START)) {
        logBuilder.append(row.split(LOG_START)[1] + "\n");
      } else {
        errorBuilder.append(row + "\n");
      }
    }
    lastLogs = logBuilder.toString();
    return errorBuilder.toString();
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
