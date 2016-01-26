package jstester;

import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import jstester.exceptions.JsTestException;
import jstester.plugins.JsTestPlugin;
import jstester.plugins.JsTestPluginAggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Javascript source tester util.
 */
public final class JsTester {

  /**
   * The path of the test_util.js
   */
  public static final String JS_TEST_UTIL = "javascript.test.test_util";
  private static final Logger LOGGER = LoggerFactory.getLogger(JsTester.class);

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
   */
  public static String runTestsAndGetErrors(String... jsFileNames)
      throws IOException {
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
   */
  public static String runTestsAndGetErrors(JsTestPluginAggregator plugins,
      String... jsFileNames) throws IOException {
    final JsFileProperties[] userCodes = JsContentsUtil.readFiles(jsFileNames);
    for (JsTestPlugin plugin : plugins) {
      plugin.eval(userCodes);
    }
    String lastStackTraces = plugins.getDefault().getLastStackTraces();
    String lastRunResults = plugins.getDefault().getLastRunResults();
    if (!"".equals(lastStackTraces)) {
      LOGGER.error(lastRunResults);
      throw new JsTestException(lastStackTraces);
    }
    return lastRunResults;
  }
}
