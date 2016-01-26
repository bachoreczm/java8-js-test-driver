package jstester.plugins.defaultplugin;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.newEngine;

import java.io.IOException;

import javax.script.ScriptException;

import jstester.JsFileProperties;
import jstester.JsContentsUtil;
import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPlugin;

public class DefaultJsTestPlugin implements JsTestPlugin {

  private static final String LOG_START = "JAVA8JSTDLOG:";
  private static final String STATISTICS_START = "JAVA8JSTDSTATISTICS:";
  private String lastStackTraces;
  private String lastLogs;
  private String statistics;
  private static final String RUNCOMMAND = "runAllTests();\ngetTestErrors();";

  @Override
  public void eval(JsFileProperties[] userCodes) throws IOException {
    if (userCodes.length == 0) {
      return;
    }
    final String userCode = JsContentsUtil.computeUserCode(userCodes);
    final JsFileProperties testUtil = JsContentsUtil.readFile(JS_TEST_UTIL);
    final String skipTests = SkipFunctionUtil.computeSkips(userCodes);
    final String runnerCode = skipTests + RUNCOMMAND;
    final String code = computeCode(testUtil, userCode, runnerCode);
    String error = evalEngine(code);
    String stacktraces = computeLogAndStatAndRemoveFromError(error);
    StackTraceProperties stackProps = new StackTraceProperties(testUtil,
        userCodes);
    lastStackTraces = StackTraceFormatter.format(stacktraces, stackProps);
  }

  private String evalEngine(final String code) {
    try {
      return (String) newEngine().eval(code);
    } catch (ScriptException e) {
      throw new JsTestException(e);
    }
  }

  /**
   * @return the last run's log messages.
   */
  public String getLog() {
    return lastLogs;
  }

  private String computeLogAndStatAndRemoveFromError(final String error) {
    StringBuilder errorBuilder = new StringBuilder();
    StringBuilder logBuilder = new StringBuilder();
    for (String row : error.split("\\n")) {
      if ("".equals(row.trim())) {
        continue;
      }
      if (row.startsWith(LOG_START)) {
        logBuilder.append(row.split(LOG_START)[1] + "\n");
      } else if (row.startsWith(STATISTICS_START)) {
        statistics = row.split(STATISTICS_START)[1] + "\n";
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

  private String formatStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("-----------\n");
    sb.append("STATISTICS:\n");
    String[] splittedStats = statistics.split("sep");
    sb.append("Passed: " + splittedStats[0] + ". ");
    sb.append("Error: " + splittedStats[1] + ". ");
    sb.append("Skipped: " + splittedStats[2]);
    return sb.toString();
  }

  @Override
  public String getName() {
    return "Javascript tester";
  }

  @Override
  public String getLastRunResults() {
    return lastLogs + formatStatistics();
  }
}
