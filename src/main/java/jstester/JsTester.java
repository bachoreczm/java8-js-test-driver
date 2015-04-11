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
   * @param srcFiles
   *          the src js files pathes (e.g.: javascript.test.file_name)
   * @return the errors string
   * @throws IOException
   *           if can not read the source files
   * @throws ScriptException
   *           if something wrong with the evaulation of the js files
   */
  public static String runTestsAndGetErrors(String testFile, String... srcFiles)
      throws IOException, ScriptException {
    final JsFileProperties[] srcCodes = getCodes(srcFiles);
    final JsFileProperties testCode = getCode(testFile);
    final String userCode = computeUserCode(testCode, srcCodes);
    final JsFileProperties testUtil = getCode("javascript.test.test_util");
    final String runningTests = "runAllTests();\ngetTestErrors();";
    final String code = computeCode(testUtil, userCode, runningTests);
    String stackTraces = (String) ENGINE.eval(code);
    StackTraceProperties stackProps = new StackTraceProperties(testCode,
        testUtil, srcCodes);
    return formattingStackTraces(stackTraces, stackProps);
  }

  private static JsFileProperties[] getCodes(String[] srcFiles)
      throws IOException {
    JsFileProperties[] codes = new JsFileProperties[srcFiles.length];
    for (int i = 0; i < srcFiles.length; ++i) {
      codes[i] = getCode(srcFiles[i]);
    }
    return codes;
  }

  private static String computeUserCode(JsFileProperties testCode,
      JsFileProperties[] srcCodes) {
    StringBuilder userCode = new StringBuilder();
    userCode.append(testCode.toString());
    for (JsFileProperties srcCode : srcCodes) {
      userCode.append("\n" + srcCode);
    }
    return userCode.toString();
  }

  private static String computeCode(JsFileProperties testUtil, String userCode,
      String runningTests) {
    return testUtil + "\n" + userCode + "\n" + runningTests;
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
