package jstester;

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

  /**
   * The path of the test_util.js
   */
  public static final String JS_TEST_UTIL = "javascript.test.test_util";

  private JsTester() {
  }

  /**
   * @return new javascript engine.
   */
  public static ScriptEngine newEngine() {
    final ScriptEngineManager manager = new ScriptEngineManager();
    return manager.getEngineByName("JavaScript");
  }

  /**
   * @param jsFileNames
   *          the js files path (e.g.: javascript.test.file_name), without the
   *          '.js'.
   * @return the errors string
   * @throws IOException
   *           if can not read the source files
   * @throws ScriptException
   *           if something wrong with the evaulation of the js files
   */
  public static String runTestsAndGetErrors(String... jsFileNames)
      throws IOException, ScriptException {
    return runTestsAndGetErrors(JsTestPluginAggregator.empty(), jsFileNames);
  }

  /**
   * @param plugins
   *          the {@link JsTestPlugin}s, you want to use.
   * @param jsFileNames
   *          the js files path (e.g.: javascript.test.file_name), without the
   *          '.js'.
   * @return the errors string
   * @throws IOException
   *           if can not read the source files
   * @throws ScriptException
   *           if something wrong with the evaulation of the js files
   */
  public static String runTestsAndGetErrors(JsTestPluginAggregator plugins,
      String... jsFileNames) throws IOException, ScriptException {
    final JsFileProperties[] userCodes = getCodes(jsFileNames);
    for (JsTestPlugin plugin : plugins) {
      plugin.eval(userCodes);
    }
    return plugins.getDefault().getLastStackTraces();
  }

  private static JsFileProperties[] getCodes(String[] srcFiles)
      throws IOException {
    JsFileProperties[] codes = new JsFileProperties[srcFiles.length];
    for (int i = 0; i < srcFiles.length; ++i) {
      codes[i] = getCode(srcFiles[i]);
    }
    return codes;
  }

  /**
   * @param codes
   *          the javascript codes
   * @return concatenating the given codes.
   */
  public static String computeUserCode(JsFileProperties[] codes) {
    StringBuilder userCode = new StringBuilder();
    for (int i = 0; i < codes.length; ++i) {
      if (i > 0) {
        userCode.append("\n");
      }
      userCode.append(codes[i].toString());
    }
    return userCode.toString();
  }

  /**
   * Reading a js file from the classpath.
   *
   * @param path
   *          file's path
   * @return a {@link JsFileProperties} which contains the js file's content
   * @throws IOException
   *           if an I/O error occurs
   */
  public static JsFileProperties getCode(String path) throws IOException {
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
