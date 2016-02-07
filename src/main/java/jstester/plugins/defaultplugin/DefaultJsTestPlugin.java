package jstester.plugins.defaultplugin;

import static jstester.JsTester.JS_TEST_UTIL;
import static jstester.JsTester.newEngine;

import javax.script.ScriptException;

import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPlugin;

public class DefaultJsTestPlugin implements JsTestPlugin {

  private static final String LOG_START = "JAVA8JSTDLOG:";
  private static final String STATISTICS_START = "JAVA8JSTDSTATISTICS:";
  private static final String RUNCOMMAND = "runAllTests();\ngetTestErrors();";

  private JsFileCollection userCodes;

  private String rawJsCode;

  private String resultOfEvaluation;

  private String logs;
  private String statistics;
  private String errors;

  private String lastStackTraces;

  /**
   * Initialize the default plugin, which runs the js-tests.
   *
   * @param userCodes
   *          the collection of the usercodes.
   */
  public DefaultJsTestPlugin(JsFileCollection userCodes) {
    this.userCodes = userCodes;
  }

  @Override
  public void eval() {
    if (userCodes.size() == 0) {
      return;
    }
    preProcess();
    evalEngine();
    postProcess();
  }

  private void preProcess() {
    String rawUserCode = userCodes.computeContent();
    String skipTests = SkipFunctionUtil.computeSkips(userCodes);
    String runnerCode = skipTests + RUNCOMMAND;
    rawJsCode = JS_TEST_UTIL + "\n" + rawUserCode + "\n" + runnerCode;
  }

  private void evalEngine() {
    try {
      resultOfEvaluation = (String) newEngine().eval(rawJsCode);
    } catch (ScriptException e) {
      throw new JsTestException(e);
    }
  }

  private void postProcess() {
    computeStatistics();
    computeLogs();
    computeErrors();

    formatStacktrace();
  }

  private void computeStatistics() {
    for (String row : resultOfEvaluation.split("\\n")) {
      if (row.startsWith(STATISTICS_START)) {
        statistics = row.split(STATISTICS_START)[1] + "\n";
        return;
      }
    }
  }

  private void computeLogs() {
    StringBuilder logBuilder = new StringBuilder();
    for (String row : resultOfEvaluation.split("\\n")) {
      if (row.startsWith(LOG_START)) {
        logBuilder.append(row.split(LOG_START)[1] + "\n");
      }
    }
    logs = logBuilder.toString();
  }

  private void computeErrors() {
    StringBuilder errorBuilder = new StringBuilder();
    for (String row : resultOfEvaluation.split("\\n")) {
      if ("".equals(row.trim())) {
        continue;
      }
      if (!row.startsWith(LOG_START) && !row.startsWith(STATISTICS_START)) {
        errorBuilder.append(row + "\n");
      }
    }
    errors = errorBuilder.toString();
  }

  private void formatStacktrace() {
    StacktraceFormatter formatter = new StacktraceFormatter(errors);
    lastStackTraces = formatter.formatStacktraceRows(userCodes);
  }

  /**
   * @return the last run's log messages.
   */
  public String getLog() {
    return logs;
  }

  /**
   * @return the last {@link #eval(jstester.JsFile[])} result.
   */
  public String getLastStackTraces() {
    return lastStackTraces;
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
    return logs + formatStatistics();
  }
}
