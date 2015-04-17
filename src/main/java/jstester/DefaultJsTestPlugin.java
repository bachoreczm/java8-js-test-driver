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
    final String skipTests = computeSkipTestFunctions(userCodes);
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String runnerCode = skipTests + runningTests;
    final String code = computeCode(testUtil, userCode, runnerCode);
    String stackTraces = (String) newEngine().eval(code);
    StackTraceProperties stackProps = new StackTraceProperties(testUtil,
        userCodes);
    lastStackTraces = formattingStackTraces(stackTraces, stackProps);
  }

  private String computeSkipTestFunctions(JsFileProperties[] userCodes) {
    StringBuilder skipTests = new StringBuilder();
    for (JsFileProperties file : userCodes) {
      String content = file.toString();
      String[] lines = content.split("\\n");
      for (int i = 0; i < lines.length; ++i) {
        if (isSkipComment(lines, i) && isFunction(lines[i + 1])) {
          skipFunction(skipTests, lines[i + 1]);
        }
      }
    }
    return skipTests.toString();
  }

  private void skipFunction(StringBuilder skipTests, String line) {
    String[] dotSplit = line.trim().split("\\.");
    String testClass = "'" + dotSplit[0] + "'";
    String testFunc = "'" + dotSplit[2].split("=")[0].trim() + "'";
    String jsCode = "skipTestFunction(" + testClass + ", " + testFunc + ");\n";
    skipTests.append(jsCode);
  }

  private boolean isFunction(final String line) {
    String[] dotSplit = line.trim().split("\\.");
    String[] equalSplit = line.split("=");
    if (prototypeLine(dotSplit) && functionLine(equalSplit)) {
      return true;
    }
    return false;
  }

  private boolean prototypeLine(String[] dotSplit) {
    return dotSplit.length == 3 && dotSplit[1].equals("prototype");
  }

  private boolean functionLine(String[] equalSplit) {
    int length = equalSplit.length;
    return length == 2 && equalSplit[1].trim().startsWith("function");
  }

  private boolean isSkipComment(String[] lines, int index) {
    String line = lines[index];
    boolean lastLine = index == (lines.length - 1);
    return !lastLine && isComment(line) && line.contains("skip");
  }

  private boolean isComment(String line) {
    return line.trim().startsWith("//");
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
