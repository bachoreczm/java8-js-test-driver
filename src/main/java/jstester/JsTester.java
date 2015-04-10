package jstester;

import static jstester.StackTraceFormatter.formattingStackTraces;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Javascript source tester util.
 */
public final class JsTester {

  private static final ScriptEngine ENGINE = getJsEngine();

  private JsTester() {
  }

  private static ScriptEngine getJsEngine() {
    final ScriptEngineManager manager = new ScriptEngineManager();
    return manager.getEngineByName("JavaScript");
  }

  /**
   * @param testFile
   *          the test js file path (e.g.: javascript.test.file_name)
   * @param srcFile
   *          the src js file path (e.g.: javascript.test.file_name)
   * @return the errors string
   * @throws IOException
   *           if can not read the source files
   * @throws ScriptException
   *           if something wrong with the evaulation of the js files
   */
  public static String runtestsAndGetErrors(String testFile, String srcFile)
      throws IOException, ScriptException {
    final JsFileProperties srcCode = getCode(srcFile);
    final JsFileProperties testCode = getCode(testFile);
    final String userCode = testCode + "\n" + srcCode;
    final JsFileProperties testUtil = getCode("javascript.test.test_util");
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String code = testUtil + "\n" + userCode + "\n" + runningTests;
    String stackTraces = (String) ENGINE.eval(code);
    StackTraceProperties stackProps = new StackTraceProperties(testCode,
        testUtil, srcCode.getFileName());
    return formattingStackTraces(stackTraces, stackProps);
  }

  /**
   * @param testFile
   *          the test js file path (e.g.: javascript.test.file_name)
   * @return the errors string
   * @throws IOException
   *           if can not read the source files
   * @throws ScriptException
   *           if something wrong with the evaulation of the js files
   */
  public static String runtestsAndGetErrors(String testFile)
      throws IOException, ScriptException {
    final JsFileProperties testCode = getCode(testFile);
    final String userCode = testCode.toString();
    final JsFileProperties testUtil = getCode("javascript.test.test_util");
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String code = testUtil + "\n" + userCode + "\n" + runningTests;
    String stackTraces = (String) ENGINE.eval(code);
    StackTraceProperties stackProps = new StackTraceProperties(testCode,
        testUtil, "");
    return formattingStackTraces(stackTraces, stackProps);
  }

  private static JsFileProperties getCode(String path) throws IOException {
    String fileName = "/" + path.replaceAll("\\.", "/") + ".js";
    InputStream is = JsTester.class.getResourceAsStream(fileName);
    InputStreamReader isReader = new InputStreamReader(is);
    BufferedReader reader = new BufferedReader(isReader);
    final StringBuilder sb = new StringBuilder();
    String line = reader.readLine();
    int lineNumber = 0;
    while (line != null) {
      sb.append(line + "\n");
      line = reader.readLine();
      ++lineNumber;
    }
    return new JsFileProperties(path, lineNumber, sb.toString());
  }
}
